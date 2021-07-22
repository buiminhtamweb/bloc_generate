package com.github.buiminhtamweb.blocgenerate.model

data class GeneratorConfig(
    val packageName: String = "",
    val className: String = "",
    val addDependencyEnabled: Boolean = true,
    val mobiusComponents: List<MobiusComponent> = emptyList()
) {

    val isEventGenerated: Boolean
        get() = mobiusComponents.contains(MobiusComponent.Bloc)

    val isEffectGenerated: Boolean
        get() = mobiusComponents.contains(MobiusComponent.Route)
}
