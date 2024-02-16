package com.customheroes.catalog.model.postgres_model

import jakarta.persistence.*


@Entity
@Table(name = "filter_table")
class Filter (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filter_id")
    var id: Int? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "tag_id")
    var tag: Tag? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "figure_id")
    var figure: Figure? = null
)