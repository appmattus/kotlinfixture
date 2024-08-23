/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.detomarco.kotlinfixture.datafaker.option

import net.datafaker.providers.base.Finance

/**
 * Different types of [CreditCard]
 */
@Suppress("unused")
enum class CreditCard(internal val creditCardType: Finance.CreditCardType?) {
    Any(null),

    AmericanExpress(Finance.CreditCardType.AMERICAN_EXPRESS),

    @Suppress("SpellCheckingInspection")
    Dankort(Finance.CreditCardType.DANKORT),

    DinersClub(Finance.CreditCardType.DINERS_CLUB),

    Discover(Finance.CreditCardType.DISCOVER),

    @Suppress("SpellCheckingInspection")
    Forbrugsforeningen(Finance.CreditCardType.FORBRUGSFORENINGEN),

    JCB(Finance.CreditCardType.JCB),

    Laser(Finance.CreditCardType.LASER),

    Mastercard(Finance.CreditCardType.MASTERCARD),

    Solo(Finance.CreditCardType.SOLO),

    Switch(Finance.CreditCardType.SWITCH),

    Visa(Finance.CreditCardType.VISA)
}
