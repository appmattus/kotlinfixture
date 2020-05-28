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

package com.appmattus.kotlinfixture.decorator.fake.javafaker.option

import com.github.javafaker.CreditCardType

@Suppress("unused")
enum class CreditCard(internal val creditCardType: CreditCardType?) {
    Any(null),

    AmericanExpress(CreditCardType.AMERICAN_EXPRESS),
    Dankort(CreditCardType.DANKORT),
    DinersClub(CreditCardType.DINERS_CLUB),
    Discover(CreditCardType.DISCOVER),
    Forbrugsforeningen(CreditCardType.FORBRUGSFORENINGEN),
    JCB(CreditCardType.JCB),
    Laser(CreditCardType.LASER),
    Mastercard(CreditCardType.MASTERCARD),
    Solo(CreditCardType.SOLO),
    Switch(CreditCardType.SWITCH),
    Visa(CreditCardType.VISA)
}
