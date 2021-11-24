#Feature: Pay permit
#
  #AS
  #a car driver
  #
  #I WANT TO
  #pay for a permit
  #
  #SO THAT
  #I can park the car without being fined
#
#
#@hardCodedHexagon
#Scenario: Hardcoded payment
#
#Given it does not exist any rate repository
#And it does not exist any permit repository
#And it does not exist any payment recipient
#
#When I do the following pay permit request:
#| permitId               | paymentCardNumber | paymentCardCvv | paymentCardExpirationDate |
#| 9999252525202109301750 | 9876543210654321  | 321            | 2022/11                   |
#
#Then I should get the following pay permit response:
#| permitId               | carPlate | startingDateTime | endingDateTime   | rateName   | price |
#| 9999252525202109301750 | 9999ZZZ  | 2021/09/30 17:50 | 2021/09/30 19:50 | GREEN_ZONE | 1.90  |
#
#
#
#
#
#
#Scenario: At Blue Zone during 1h 30m
#
#Given there exist these rates:
#| name      | amountPerHour | minMinutesAllowed | maxMinutesAllowed |
#| BLUE_ZONE | 0.70          | 30                | 120               |
#
#And next paymentId is "76d0f01a-25a6-4bd0-88f5-0cd026a06163"
#
#When I do the following permit issuing request at "2021/09/27 08:00":
#| carPlate | rateName  | endingDateTime   | paymentCardNumber | paymentCardCvv | paymentCardExpirationDate |
#| 6989JJH  | BLUE_ZONE | 2021/09/27 09:30 | 1234567890123456  | 123            | 2025/06                   |
#
#Then I should get the following permit ticket:
#| code                     | carPlate | startingDateTime | endingDateTime   | rateName  | price |
#| PT6989090907202109270800 | 6989JJH  | 2021/09/27 08:00 | 2021/09/27 09:30 | BLUE_ZONE | 1.05  |
#
#And the following permit should have been stored:
#| carPlate | startingDateTime | endingDateTime   | rateName  | paymentId                            |
#| 6989JJH  | 2021/09/27 08:00 | 2021/09/27 09:30 | BLUE_ZONE | 76d0f01a-25a6-4bd0-88f5-0cd026a06163 |
#
#And the following payment should have been done:
#| id                                   | payerCardNumber  | amount |
#| 76d0f01a-25a6-4bd0-88f5-0cd026a06163 | 1234567890123456 | 1.05   |