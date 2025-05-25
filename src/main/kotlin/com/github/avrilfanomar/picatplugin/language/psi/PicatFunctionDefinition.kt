package com.github.avrilfanomar.picatplugin.language.psi

/**
 * Interface for Picat function definition PSI elements.
 */
interface PicatFunctionDefinition : PicatFact {
    /**
     * Returns the name of the function.
     */
    fun getName(): String?

    /**
     * Returns the arity of the function (number of arguments).
     */
    fun getArity(): Int

    /**
     * Returns the atom of the function.
     */
    fun getAtom(): PicatAtom?

    /**
     * Returns the argument list of the function, if any.
     */
    fun getArgumentList(): PicatArgumentList?

    /**
     * Returns the body of the function.
     */
    fun getBody(): PicatFunctionBody?
}
