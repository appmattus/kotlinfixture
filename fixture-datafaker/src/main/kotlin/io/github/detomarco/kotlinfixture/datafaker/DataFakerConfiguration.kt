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

package io.github.detomarco.kotlinfixture.datafaker

import io.github.detomarco.kotlinfixture.config.ConfigurationBuilder
import io.github.detomarco.kotlinfixture.datafaker.option.CreditCard
import io.github.detomarco.kotlinfixture.datafaker.option.IpAddress
import io.github.detomarco.kotlinfixture.datafaker.option.Password
import io.github.detomarco.kotlinfixture.datafaker.option.Temperature
import io.github.detomarco.kotlinfixture.datafaker.option.UserAgent
import io.github.detomarco.kotlinfixture.decorator.fake.fakeStrategy
import net.datafaker.Faker
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Configuration for the DataFakerStrategy
 */
data class DataFakerConfiguration internal constructor(
    internal val creditCard: CreditCard = CreditCard.Any,
    internal val ipAddress: IpAddress = IpAddress.V4,
    internal val isbn10Separator: Boolean = false,
    internal val isbn13Separator: Boolean = false,
    internal val locale: Locale = Locale.ENGLISH,
    internal val password: Password = Password(),
    internal val temperature: Temperature = Temperature.Celsius,
    internal val userAgent: UserAgent = UserAgent.Any,
    internal val properties: Map<String, Faker.(DataFakerConfiguration) -> Any?> = defaultMap
) {
    private companion object {

        private fun entry(
            propertyName: String,
            generator: Faker.(DataFakerConfiguration) -> Any?
        ): Pair<String, Faker.(DataFakerConfiguration) -> Any?> = Pair(propertyName, generator)

        private val defaultMap = mapOf(
            // Address
            entry("buildingNumber") { address().buildingNumber() },
            entry("city") { address().city() },
            entry("cityName") { address().cityName() },
            entry("cityPrefix") { address().cityPrefix() },
            entry("citySuffix") { address().citySuffix() },
            entry("country") { address().country() },
            entry("countryCode") { address().countryCode() },
            entry("fullAddress") { address().fullAddress() },
            entry("latitude") { address().latitude() },
            entry("longitude") { address().longitude() },
            entry("secondaryAddress") { address().secondaryAddress() },
            entry("state") { address().state() },
            entry("stateAbbr") { address().stateAbbr() },
            entry("streetAddress") { address().streetAddress() },
            entry("streetAddressNumber") { address().streetAddressNumber() },
            entry("streetName") { address().streetName() },
            entry("streetPrefix") { address().streetPrefix() },
            entry("streetSuffix") { address().streetSuffix() },
            entry("timeZone") { address().timeZone() },
            entry("zipCode") { address().zipCode() },

            // Avatar
            entry("image") { avatar().image() },

            // Aviation
            entry("aircraft") { aviation().aircraft() },
            entry("airport") { aviation().airport() },
            @Suppress("SpellCheckingInspection")
            (entry("METAR") { aviation().METAR() }),

            // Business
            entry("creditCardExpiry") { business().creditCardExpiry() },
            entry("creditCardNumber") { business().creditCardNumber() },
            entry("creditCardType") { business().creditCardType() },

            // Code
            (entry("asin") { code().asin() }),
            entry("ean13") { code().ean13() },
            entry("ean8") { code().ean8() },
            (entry("gtin13") { code().gtin13() }),
            (entry("gtin8") { code().gtin8() }),
            (entry("imei") { code().imei() }),
            entry("isbn10") { code().isbn10(it.isbn10Separator) },
            entry("isbn13") { code().isbn13(it.isbn13Separator) },
            entry("isbnGroup") { code().isbnGroup() },
            entry("isbnGs1") { code().isbnGs1() },
            entry("isbnRegistrant") { code().isbnRegistrant() },

            // Commerce
            entry("price") { commerce().price() },
            entry("productName") { commerce().productName() },

            // Company
            entry("logo") { company().logo() },
            entry("url") { company().url() },

            // Country
            entry("countryCode2") { country().countryCode2() },
            entry("countryCode3") { country().countryCode3() },
            entry("currency") { country().currency() },
            entry("currencyCode") { country().currencyCode() },
            entry("flag") { country().flag() },

            // Currency
            entry("currencyName") {
                money().currency()
            },

            // DateAndTime
            entry("birthday") { timeAndDate().birthday() },
            entry("dateOfBirth") { timeAndDate().birthday() },
            entry("dob") { timeAndDate().birthday() },
            entry("age") {
                ChronoUnit.YEARS.between(timeAndDate().birthday(), LocalDate.now())
            },

            // Demographic
            entry("maritalStatus") { demographic().maritalStatus() },
            entry("race") { demographic().race() },
            entry("sex") { demographic().sex() },

            // Educator
            entry("secondarySchool") { educator().secondarySchool() },
            entry("university") { educator().university() },

            // File
            entry("fileName") { file().fileName() },
            entry("mimeType") { file().mimeType() },

            // Finance
            entry("bic") { finance().bic() },
            entry("creditCard") {
                when (it.creditCard) {
                    CreditCard.Any -> finance().creditCard()
                    else -> finance().creditCard(it.creditCard.creditCardType)
                }
            },
            (entry("iban") { finance().iban() }),

            // IdNumber
            entry("ssn") { idNumber().ssnValid() },
            entry("svSeSsn") {
                val faker = Faker(Locale("sv", "SE"))
                faker.idNumber().valid()
            },

            // Internet
            entry("domainName") { internet().domainName() },
            entry("emailAddress") { internet().emailAddress() },
            entry("ipAddress") {
                when (it.ipAddress) {
                    IpAddress.V4 -> internet().ipV4Address()
                    IpAddress.V6 -> internet().ipV6Address()
                }
            },
            entry("ipV4Address") { internet().ipV4Address() },
            (entry("ipV4Cidr") { internet().ipV4Cidr() }),
            entry("ipV6Address") { internet().ipV6Address() },
            (entry("ipV6Cidr") { internet().ipV6Cidr() }),
            entry("macAddress") { internet().macAddress() },
            entry("password") {
                internet().password(
                    it.password.minimumLength,
                    it.password.maximumLength,
                    it.password.includeUppercase,
                    it.password.includeSpecial,
                    it.password.includeDigit
                )
            },
            entry("slug") { internet().slug() },
            entry("userAgent") {
                when (it.userAgent) {
                    UserAgent.Any -> internet().userAgent()
                    else -> internet().userAgent(it.userAgent.userAgent)
                }
            },

            // Name
            entry("firstName") { name().firstName() },
            entry("fullName") { name().fullName() },
            entry("lastName") { name().lastName() },
            entry("name") { name().name() },
            entry("nameWithMiddle") { name().nameWithMiddle() },
            entry("prefix") { name().prefix() },
            entry("suffix") { name().suffix() },

            // Phone Number
            entry("cellPhone") { phoneNumber().cellPhone() },
            entry("phoneNumber") { phoneNumber().phoneNumber() },

            // Stock
            @Suppress("SpellCheckingInspection")
            (entry("nsdqSymbol") { stock().nsdqSymbol() }),
            entry("nyseSymbol") { stock().nyseSymbol() },

            // Weather
            entry("temperature") {
                when (it.temperature) {
                    Temperature.Celsius -> weather().temperatureCelsius()
                    Temperature.Fahrenheit -> weather().temperatureFahrenheit()
                }
            },
            entry("temperatureCelsius") { weather().temperatureCelsius() },
            entry("temperatureFahrenheit") { weather().temperatureFahrenheit() }
        )
    }
}

/**
 * Fake object generation with `dataFakerStrategy`
 *
 * The faker intercepts the generation of named properties so their values can be replaced with fake data generated by
 * the [Java Faker](https://github.com/DiUS/java-faker) library
 */
fun ConfigurationBuilder.dataFakerStrategy(configuration: DataFakerConfigurationBuilder.() -> Unit = {}) {
    fakeStrategy(
        (fakeStrategy as? DataFakerStrategy)?.new(configuration) ?: DataFakerStrategy().new(configuration)
    )
}
