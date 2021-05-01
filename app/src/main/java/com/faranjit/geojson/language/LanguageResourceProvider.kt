package com.faranjit.geojson.language

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
object LanguageResourceProvider {

    private var dictionary: Map<String, LinkedHashMap<String, String>> = HashMap()

    private fun getString(parent: String, key: String): String {
        var value = key
        val root = dictionary[parent]
        if (root != null && root[key] != null) {
            value = root[key]!!
        }

        return value
    }

    /**
     * Sets language pack that contains key value pairs
     * When language changes this should be called
     * @param dict new language pack
     */
    fun setDictionary(dict: Map<String, LinkedHashMap<String, String>>) {
        this.dictionary = dict
    }

    /**
     * Gets value of given key from the dictionary.
     * @param key name of value in the dictionary
     * @return String
     */
    fun getString(key: String): String {
        val parentIndex = key.lastIndexOf('.')
        return getString(key.substring(0 until parentIndex), key.substring(parentIndex + 1))
    }
}