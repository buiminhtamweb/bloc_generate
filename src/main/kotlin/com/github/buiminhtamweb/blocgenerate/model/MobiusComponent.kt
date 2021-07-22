package com.github.buiminhtamweb.blocgenerate.model

sealed class MobiusComponent {
    object Bloc : MobiusComponent()

    object Route : MobiusComponent()

    object View : MobiusComponent()

//  object Init : MobiusComponent()
//
//  object Update : MobiusComponent()
//
//  object EffectHandler : MobiusComponent()
}
