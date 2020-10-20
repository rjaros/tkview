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
package se.skoview.app

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.html.header
import pl.treksoft.kvision.html.main
import pl.treksoft.kvision.module
import pl.treksoft.kvision.pace.Pace
import pl.treksoft.kvision.pace.PaceOptions
import pl.treksoft.kvision.panel.ContainerType
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import se.skoview.rivta.domainView
import se.skoview.rivta.headerNav
import se.skoview.rivta.contractListView
import tabs.rivta.domainListView

// todo: Läs ner TPDB domainId dynamiskt: https://integrationer.tjansteplattform.se/tpdb/tpdbapi.php/api/v1/domains
// todo: hippolänkar till kontrakt
// todo: Lös detta med CORS. Ev ta in REST-lösningen från Roberts showcase. Annat alternativ är att rivta.se dygnsvis hämtar filen och lagrar lokalt... Det är nog bäst.
// done: Back-knapp
// done: Paginering
// done: Red ut kotlins serialization och instansiering och defalutvärden

class App : Application() {
    init {
        require("css/helloworld.css")
    }

    override fun start() {
        Pace.init(require("pace-progressbar/themes/green/pace-theme-bounce.css"))
        Pace.setOptions(PaceOptions(manual = true))
        RivManager.initialize()
        root("app", containerType = ContainerType.NONE, addRow = false) {
            header(RivManager.rivStore) { state ->
                // The old RivTaMainPage
                headerNav(state)
            }
            /**
             * main() below subscribe to state changes.
             * The application main dispatcher that sets the view in the application will end up here where * the correct page can be rendered.
             */
            main(RivManager.rivStore) { state ->
                println("In main()")
                if (!state.appLoading) {
                    when (state.view) {
                        View.HOME -> {
                            println("HOME / URL specified")
                            // dummyView(state, "HOME")
                        }
                        View.DOMAIN_LIST -> {
                            println("Donain list view")
                            // dummyView(state, "DOMAIN_LIST")
                            domainListView(state)
                        }
                        View.CONTRACT_LIST -> {
                            println("Contract list view")
                            contractListView(state)
                            // dummyView(state, "CONTRACT_LIST")
                        }
                        View.DOMAIN -> {
                            println("Domain view")
                            domainView(state)
                            // dummyView(state, "DOMAIN")
                        }
                    }
                }
            }
            println("footer()")
            // footer()
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
