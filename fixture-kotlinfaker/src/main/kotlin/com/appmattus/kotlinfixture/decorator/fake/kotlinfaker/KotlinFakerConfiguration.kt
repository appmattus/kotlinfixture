/*
 * Copyright 2026 Appmattus Limited
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

package com.appmattus.kotlinfixture.decorator.fake.kotlinfaker

import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.decorator.fake.fakeStrategy
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.CreditCard
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.IpAddress
import com.appmattus.kotlinfixture.decorator.fake.kotlinfaker.option.UserAgent
import io.github.serpro69.kfaker.Faker
import java.util.Locale

/**
 * Configuration for the KotlinFakerStrategy
 */
data class KotlinFakerConfiguration internal constructor(
    internal val creditCard: CreditCard = CreditCard.Any,
    internal val ipAddress: IpAddress = IpAddress.V4,
    internal val locale: Locale = Locale.ENGLISH,
    internal val userAgent: UserAgent = UserAgent.Any,
    internal val properties: Map<String, Faker.(KotlinFakerConfiguration) -> Any?> = defaultMap
) {
    private companion object {

        private const val MINIMUM_AGE = 18
        private const val MAXIMUM_AGE = 65
        private const val MIME_TYPE_CATEGORIES = 5

        private const val LUHN_PLACEHOLDER = 'L'
        private const val DECIMAL = 10
        private const val MAX_DIGIT = 9

        /**
         * Kotlin Faker returns credit card numbers from its pattern without removing the surrounding `/` or replacing
         * the `L` placeholder with the Luhn check digit, for example `/3404-453746-2331L/`, so finish them here.
         */
        private fun String.withLuhnCheckDigit(): String {
            val number = trim('/')
            if (number.lastOrNull() != LUHN_PLACEHOLDER) return number

            val digits = number.filter(Char::isDigit).map(Char::digitToInt)
            val sum = digits.reversed().mapIndexed { index, digit ->
                if (index % 2 == 0) {
                    (digit * 2).let { if (it > MAX_DIGIT) it - MAX_DIGIT else it }
                } else {
                    digit
                }
            }.sum()

            return number.dropLast(1) + ((DECIMAL - sum % DECIMAL) % DECIMAL)
        }

        private fun entry(
            propertyName: String,
            generator: Faker.(KotlinFakerConfiguration) -> Any?
        ): Pair<String, Faker.(KotlinFakerConfiguration) -> Any?> = Pair(propertyName, generator)

        private val defaultMap = mapOf(
            // Address
            entry("buildingNumber") { address.buildingNumber() },
            entry("city") { address.city() },
            entry("cityPrefix") { address.cityPrefix() },
            entry("citySuffix") { address.citySuffix() },
            entry("community") { address.community() },
            entry("country") { address.country() },
            entry("countryCode") { address.countryCode() },
            entry("defaultCountry") { address.defaultCountry() },
            entry("fullAddress") { address.fullAddress() },
            entry("mailbox") { address.mailbox() },
            entry("postcode") { address.postcode() },
            entry("secondaryAddress") { address.secondaryAddress() },
            entry("state") { address.state() },
            entry("stateAbbr") { address.stateAbbr() },
            entry("streetAddress") { address.streetAddress() },
            entry("streetName") { address.streetName() },
            entry("streetSuffix") { address.streetSuffix() },
            entry("timeZone") { address.timeZone() },
            entry("zipCode") { address.postcode() },

            // Bank
            entry("bankName") { bank.name() },
            entry("bic") { bank.swiftBic() },

            // Barcode
            entry("ean13") { barcode.ean13() },
            entry("ean8") { barcode.ean8() },
            entry("isbn") { barcode.isbn() },
            @Suppress("SpellCheckingInspection")
            entry("ismn") { barcode.ismn() },
            @Suppress("SpellCheckingInspection")
            entry("issn") { barcode.issn() },
            entry("upcA") { barcode.upcA() },
            entry("upcE") { barcode.upcE() },

            // Business
            entry("creditCardNumber") { business.creditCardNumbers() },
            entry("creditCardType") { business.creditCardTypes() },

            // Code
            @Suppress("SpellCheckingInspection")
            entry("asin") { code.asin() },

            // Commerce
            entry("brand") { commerce.brand() },
            entry("department") { commerce.department() },
            entry("productName") { commerce.productName() },
            entry("promotionCode") { commerce.promotionCode() },
            entry("vendor") { commerce.vendor() },

            // Company
            entry("companyName") { company.name() },
            entry("industry") { company.industry() },
            entry("profession") { company.profession() },

            // Crypto
            entry("md5") { crypto.md5() },
            entry("sha1") { crypto.sha1() },
            entry("sha224") { crypto.sha224() },
            entry("sha256") { crypto.sha256() },
            entry("sha384") { crypto.sha384() },
            entry("sha512") { crypto.sha512() },

            // Currency
            entry("currency") { currency.name() },
            entry("currencyCode") { currency.code() },
            entry("currencyName") { currency.name() },
            entry("currencySymbol") { currency.symbol() },

            // Demographic
            entry("age") { random.nextInt(MINIMUM_AGE, MAXIMUM_AGE).toLong() },
            entry("demonym") { demographic.demonym() },
            entry("educationalAttainment") { demographic.educationalAttainment() },
            entry("maritalStatus") { demographic.maritalStatus() },
            entry("race") { demographic.race() },
            entry("sex") { demographic.sex() },

            // Educator
            entry("campus") { educator.campus() },
            entry("courseName") { educator.courseName() },
            entry("degree") { educator.degree() },
            entry("primarySchool") { educator.primarySchool() },
            entry("secondarySchool") { educator.secondarySchool() },
            entry("subject") { educator.subject() },
            entry("university") { educator.university() },

            // File
            entry("fileExtension") { file.extension() },
            entry("mimeType") {
                with(file.mimeType) {
                    when (random.nextInt(MIME_TYPE_CATEGORIES)) {
                        0 -> application()
                        1 -> audio()
                        2 -> image()
                        3 -> text()
                        else -> video()
                    }
                }
            },

            // Finance
            entry("creditCard") {
                finance.creditCard(it.creditCard.type ?: random.randomValue(CreditCard.types)).withLuhnCheckDigit()
            },
            entry("stockMarket") { finance.stockMarket() },
            entry("ticker") { finance.ticker() },

            // Gender
            entry("gender") { gender.binaryTypes() },

            // Internet
            entry("domainName") { internet.domain() },
            entry("domainSuffix") { internet.domainSuffix() },
            entry("email") { internet.email() },
            entry("emailAddress") { internet.email() },
            entry("ipAddress") {
                when (it.ipAddress) {
                    IpAddress.V4 -> internet.iPv4Address()
                    IpAddress.V6 -> internet.iPv6Address()
                }
            },
            entry("ipV4Address") { internet.iPv4Address() },
            entry("ipV6Address") { internet.iPv6Address() },
            entry("macAddress") { internet.macAddress() },
            entry("privateIpV4Address") { internet.privateIPv4Address() },
            entry("publicIpV4Address") { internet.publicIPv4Address() },
            entry("safeEmail") { internet.safeEmail() },
            entry("slug") { internet.slug() },
            entry("userAgent") {
                internet.userAgent(it.userAgent.vendor ?: random.randomValue(UserAgent.vendors))
            },

            // Name
            entry("firstName") { name.firstName() },
            entry("fullName") { name.name() },
            entry("lastName") { name.lastName() },
            entry("name") { name.name() },
            entry("nameWithMiddle") { name.nameWithMiddle() },

            // Phone Number
            entry("cellPhone") { phoneNumber.cellPhone.number() },
            entry("phoneNumber") { phoneNumber.phoneNumber() }
        )
    }
}

/**
 * Fake object generation with `kotlinFakerStrategy`
 *
 * The faker intercepts the generation of named properties so their values can be replaced with fake data generated by
 * the [Kotlin Faker](https://github.com/serpro69/kotlin-faker) library
 */
fun ConfigurationBuilder.kotlinFakerStrategy(configuration: KotlinFakerConfigurationBuilder.() -> Unit = {}) {
    fakeStrategy(
        (fakeStrategy as? KotlinFakerStrategy)?.new(configuration) ?: KotlinFakerStrategy().new(configuration)
    )
}
