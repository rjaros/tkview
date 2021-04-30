/**
 * Copyright (C) 2020 Lars Erik Röjerås
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("SENSELESS_COMPARISON")

package se.skoview.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import se.skoview.app.getSync
import kotlin.collections.List

val BbDomainArr = mutableListOf<ServiceDomain>()
val BbDomainMap = mutableMapOf<String, ServiceDomain>()

fun bitbucketLoad() {
    // fun load(callback: () -> Unit) {
    println("In bitBucketLoad()")

    val json = Json { allowStructuredMapKeys = true }

    val urlTestDomain = "https://api.bitbucket.org/2.0/repositories/svrlro/riv.clinicalprocess.logistics.test"
    var response = getSync(urlTestDomain)
    json.decodeFromString(BbDomain.serializer(), response)

    val urlDomains = "https://api.bitbucket.org/2.0/repositories/rivta-domains"
    // val url = "${getBaseUrl()}/http://api.ntjp.se/dominfo/v1/servicedomains.json"

    // Older version which I try again to get it to create the actual parsed objects
    response = getSync(urlDomains)
    println("Size of response is: ${response.length}")
    // val serviceDomains: List<ServiceDomain> = json.decodeFromString(ListSerializer(ServiceDomain.serializer()), response)

    var bbDomainPagination: BbDomainPagination = json.decodeFromString(BbDomainPagination.serializer(), response)

    while (bbDomainPagination.next != null) {
        println("One more turn fetching from bitbucket")
        response = getSync(bbDomainPagination.next!!)
        bbDomainPagination = json.decodeFromString(BbDomainPagination.serializer(), response)
    }

    println("BbDomain.mapp:")
    console.log(BbDomain.mapp)

    // RivManager.domdbLoadingComplete()
}

@Serializable
data class BbDomainPagination(
    val previous: String = "",
    val size: Int = -1,
    val values: List<BbDomain>?,
    val page: Int = -1,
    val pagelen: Int = -1,
    val next: String? = null
)

@Serializable
data class BbDomain(
    val owner: Owner,
    val updated_on: String = "",
    val is_private: Boolean = false,
    val website: String? = "",
    val workspace: Workspace,
    val fork_policy: String = "",
    val project: Project,
    val description: String = "",
    val language: String = "",
    val type: String = "",
    val uuid: String = "",
    val has_issues: Boolean = false,
    val mainbranch: Mainbranch,
    val has_wiki: Boolean = false,
    val full_name: String = "",
    val size: Int = 0,
    val created_on: String = "",
    val name: String = "",
    val links: Links? = null,
    val scm: String = "",
    val slug: String = ""
) {

    val compactName: String = name
        .substring(name.indexOf(".") + 1)
        .split(".")
        .joinToString(separator = ":")

    val meta = DomainMeta.mapp[compactName]

    init {
        mapp[compactName] = this
    }

    companion object {
        val mapp: MutableMap<String, BbDomain> = mutableMapOf()
    }
}

@Serializable
data class Owner(
    val links: Links,
    val display_name: String = "",
    val type: String = "",
    val uuid: String = "",
    val username: String = "",
    val nickname: String = "",
    val account_id: String = ""
)

@Serializable
data class Commits(val href: String = "")

@Serializable
data class Mainbranch(
    val name: String = "",
    val type: String = ""
)

@Serializable
data class Branches(val href: String = "")

@Serializable
data class Html(val href: String = "")

@Serializable
data class Self(val href: String = "")

@Serializable
data class Avatar(val href: String = "")

@Serializable
data class Source(val href: String = "")

@Serializable
data class Watchers(val href: String = "")

@Serializable
data class Issues(val href: String = "")

@Serializable
data class Project(
    val name: String = "",
    val links: Links,
    val type: String = "",
    val uuid: String = "",
    val key: String = ""
)

@Serializable
data class CloneItem(
    val name: String = "",
    val href: String = ""
)

@Serializable
data class Links(
    val forks: Forks? = null,
    val watchers: Watchers? = null,
    val source: Source? = null,
    val avatar: Avatar? = null,
    val branches: Branches? = null,
    val pullrequests: Pullrequests? = null,
    val tags: Tags? = null,
    val downloads: Downloads? = null,
    val clone: List<CloneItem>? = null,
    val commits: Commits? = null,
    val self: Self,
    val html: Html,
    val hooks: Hooks? = null,
    val issues: Issues? = null
)

@Serializable
data class Pullrequests(val href: String = "")

@Serializable
data class Hooks(val href: String = "")

@Serializable
data class Downloads(val href: String = "")

@Serializable
data class Tags(val href: String = "")

@Serializable
data class Workspace(
    val name: String = "",
    val links: Links,
    val type: String = "",
    val uuid: String = "",
    val slug: String = ""
)

@Serializable
data class Forks(val href: String = "")
