/**
 * Copyright (C) 2013-2020 Lars Erik Röjerås
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

package se.skoview.app

import org.w3c.xhr.XMLHttpRequest
import pl.treksoft.kvision.core.Component

fun getAsync(url: String, callback: (String) -> Unit) {
    console.log("getAsync(): URL: $url")
    val xmlHttp = XMLHttpRequest()
    xmlHttp.open("GET", url)
    xmlHttp.onload = {
        if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
            callback.invoke(xmlHttp.responseText)
        }
    }
    xmlHttp.send()
}

fun getSync(url: String): String? {
    val xmlHttp = XMLHttpRequest()
    xmlHttp.open("GET", url, false)
    xmlHttp.send(null)

    return if (xmlHttp.status == 200.toShort()) {
        xmlHttp.responseText
    } else {
        null
    }
}

fun getHeightToRemainingViewPort(
    topComponent: Component,
    delta: Int = 48
): String {
    val occupiedViewPortArea = (topComponent.getElementJQuery()?.height() ?: 152).toInt()
    val heightToRemove = occupiedViewPortArea + delta
    return "calc(100vh - ${heightToRemove}px)"
}

fun getBaseUrl(): String  {
    // val url = "http://api.ntjp.se/dominfo/v1/servicedomain.json"
    // val url = "domdb-2020-10-20.json"
    // val url = "http://ind-dtjp-apache-api-vip.ind1.sth.basefarm.net/dominfo/v1/servicedomains.json"
    // val url = "http://localhost:4000/domdb-prod-2020-10-12.json"
    // val url = "http://qa.api.ntjp.se/dominfo/v1/servicedomains.json"
    val url = "https://rivta.se/tkview/apicache.php"
    // val url = "http://localhost:8888/apicache.php/http://qa.api.ntjp.se/dominfo/v1/servicedomains.json"
    // val url = "http://localhost:8888/apicache.php"

    return url
}