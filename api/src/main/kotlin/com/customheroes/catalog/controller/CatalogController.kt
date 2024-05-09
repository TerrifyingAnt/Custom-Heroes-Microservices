package com.customheroes.catalog.controller

import com.customheroes.catalog.model.dto.*
import com.customheroes.catalog.utils.toFigureDto
import com.customheroes.catalog.utils.toFigurePreviewDto
import com.customheroes.catalog.model.postgres_model.Filter
import com.customheroes.catalog.model.postgres_model.Tag
import com.customheroes.catalog.repository.FigureRepository
import com.customheroes.catalog.repository.FilterRepository
import com.customheroes.catalog.repository.TagRepository
import com.customheroes.catalog.utils.AppConstants
import com.customheroes.catalog.utils.toFigurePreviewWithoutLinksDto
import io.minio.GetPresignedObjectUrlArgs
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.errors.MinioException
import io.minio.http.Method
import io.minio.messages.Item
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit


@RestController
class CatalogController {
    @Autowired
    var filterRepository: FilterRepository? = null

    @Autowired
    var figureRepository: FigureRepository? = null

    @Autowired
    var tagRepository: TagRepository? = null

    @Value("\${minio.bucketName}")
    private val bucketName: String? = null

    @Autowired
    var minioClient: MinioClient? = null


    @GetMapping("/figures")
    fun getFigures(@RequestParam(name="filters", required=false, defaultValue="all") filters: List<String>, @RequestParam(name="figure_title", required=false, defaultValue="%") figureTitle: String, @RequestParam(name="page", required=true, defaultValue="0") page: Int): List<FigurePreviewDto>? {
        val pageable = PageRequest.of(page, AppConstants.NUMBER_OF_FIGURES_BY_PAGE)
        val nonNullFilterRepository = filterRepository ?: return listOf()
        val nonNullTagRepository = tagRepository ?: return listOf()
        var figuresWithoutLinks = mutableListOf<FigureWithoutLinks>()
        val resultList = mutableListOf<FigurePreviewDto>()
        var figureList = mutableListOf<Filter?>()
        val figureIdList = mutableListOf<Int>()
        if(filters.contains("all")) {
            figureList = nonNullFilterRepository.findAllByFigureTitle(pageable, figureTitle)?.toMutableList() ?: mutableListOf()
        }
        else {
            val tagList = mutableListOf<Tag>()
            filters.forEach {
                val foundTag = nonNullTagRepository.findByTitle(it)
                if (foundTag != null) {
                    tagList.add(foundTag)
                }
            }

            // TODO кринжово, потом переделать
            tagList.forEach { tag ->
            val tagId = tag.id ?: return@forEach
                val listOfId =
                    nonNullFilterRepository.findAllByTagAndFigureTitle(pageable, tagId, figureTitle)?.map { it?.figure?.id }
                        ?: listOf()
                listOfId.forEach {
                    if (it != null) {
                        figureIdList.add(it)
                    }
                }
            }

            figureIdList.toSet().forEach {
                figureList.addAll(nonNullFilterRepository.findByFigureId(it) ?: listOf())
            }
        }
        // конвертация всех моделей в FigureDto: получение списка из фигурок с тегами
        figureList.forEach { item ->
            if (figuresWithoutLinks.map { it.id }.contains(item?.figure?.id)) {
                figuresWithoutLinks.forEach { figure ->
                    if (figure.id == item?.figure?.id) {
                        if (!figure.tags.contains(item.tag)) {
                            val nonNullTag = item.tag
                            if (nonNullTag != null) {
                                figure.tags.add(nonNullTag)
                            }
                        }
                    }
                }
            } else {
                if (figuresWithoutLinks.isEmpty()) {
                    figuresWithoutLinks = mutableListOf(item.toFigureDto())
                } else {
                    figuresWithoutLinks.add(item.toFigureDto())
                }
            }
        }
        figuresWithoutLinks.forEach {
            println(it.sourcePath)
            resultList.add(setLinksForImagesAndModel(it).toFigurePreviewDto())
        }
        return resultList
    }

    @GetMapping("/figures/{id}")
    fun getFigureById(@PathVariable("id") figureId: Int): FigureDto? {
        val nonNullFigureRepository = figureRepository ?: return null
        val nonNullFilterRepository = filterRepository ?: return null
        val figure = nonNullFigureRepository.findById(figureId).get()
        val currentFiguresList = nonNullFilterRepository.findByFigure(figure)?.toList() ?: listOf()

        // получение в нормальном формате информации о фигурке:
        // для полученной фигурки есть несоклько тегов
        // далее происходит получение тегов и фигурки
        val figureSet = currentFiguresList.map { it?.figure }
        val tagSet = currentFiguresList.map { it?.tag }

        // добавление ссылок на картинки и 3д модель
        val currentFigure = figureSet[0]
        if (currentFigure != null) {
            val figureWithoutLinks = currentFigure.toFigureDto(tagSet.toList())
            return setLinksForImagesAndModel(figureWithoutLinks)
        }
        return null
    }

    @GetMapping("/tags")
    fun getAllTags(): List<TagDto>? {
        val nonNullRepository = tagRepository?: return null
        val listOfTags = nonNullRepository.findAll()
        val outputTagDtoList = mutableListOf<TagDto>()
        listOfTags.forEach {
            val nonNullTagTitle = it ?: return@forEach
            outputTagDtoList.add(TagDto(nonNullTagTitle.title ?: ""))
        }
        return outputTagDtoList
    }

    @GetMapping("/figures_preview")
    fun getFiguresPreview(@RequestParam(name="ids", required = true) ids: List<Int>, @RequestParam(name="page", required=true, defaultValue="0") page: Int): List<FigurePreviewDto>? {
        val nonNullFilterRepository = filterRepository ?: return listOf()
        val listPreviewsNoLinks = mutableListOf<FigurePreviewWithoutLinksDto>()
        val listPreviews = mutableListOf<FigurePreviewDto>()
        ids.forEach {
            val figureWithoutLinks = nonNullFilterRepository.findByFigureId(it)
            if (!figureWithoutLinks.isNullOrEmpty()) {
                val figurePreviewWithoutLink = figureWithoutLinks.toFigurePreviewWithoutLinksDto() ?: return@forEach
                listPreviewsNoLinks.add(figurePreviewWithoutLink)
            }
        }
        listPreviewsNoLinks.forEach {figurePreview ->
            val objectLinks = getImagesLinks(figurePreview.sourcePath).toList()
            val imageLinks = mutableListOf<String>()
            objectLinks.forEach {
                imageLinks.add(getTempUrl(it, "image"))
            }
            listPreviews.add(
                FigurePreviewDto(
                    id = figurePreview.id,
                    title = figurePreview.title,
                    price = figurePreview.price,
                    imageLinks = imageLinks,
                    tags = figurePreview.tags
                )
            )
        }
        return listPreviews
    }

    private fun setLinksForImagesAndModel(currentFigure: FigureWithoutLinks): FigureDto {
        val objectLinks = getImagesLinks(currentFigure.sourcePath).toList()
        val imageLinks = mutableListOf<String>()
        objectLinks.forEach {
            imageLinks.add(getTempUrl(it, "image"))
        }
        val model = getModelLink(currentFigure.sourcePath)
        var modelLink = ""
        if (model != null) {
            modelLink = getTempUrl(model, "model")
        }
        val figureDto = currentFigure.toFigureDto(imageLinks, modelLink)
        return figureDto
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, MinioException::class)
    private fun getTempUrl(fileModel: Item, type: String): String {
        val nonNullMinioRepository = minioClient ?: return ""
        val reqParams: MutableMap<String, String> = HashMap()
        if(type == "image") {
            reqParams["response-content-type"] = "$type/jpeg"
        }
        else {
            reqParams["response-content-type"] = "application/octet-stream"
        }
        val url: String = nonNullMinioRepository.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .`object`(fileModel.objectName())
                        .expiry(10, TimeUnit.MINUTES)
                        .extraQueryParams(reqParams)
                        .build())
        println(url)
        return url
    }

    fun getImagesLinks(prefix: String): List<Item> {
        val nonNullMinioClient = minioClient ?: return listOf()

        val results = ArrayList<Item>()

        val objects = nonNullMinioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(true)
                .build())

        objects.forEach { result ->
            if(result.get().objectName().endsWith(".jpg") || result.get().objectName().endsWith(".png") ||
                    result.get().objectName().endsWith(".jpeg"))
                results.add(result.get())
            }

        return results
    }

    fun getModelLink(prefix: String): Item? {
        val nonNullMinioClient = minioClient ?: return null

        val objects = nonNullMinioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(true)
                .build())

        objects.forEach { result ->
            if(result.get().objectName().endsWith(".glb") || result.get().objectName().endsWith(".obj"))
                return result.get()
        }

        return null
    }

}
