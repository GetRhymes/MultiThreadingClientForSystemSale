import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/*
    TestMultiThreadingApps - класс предназначен для тестирования серверного приложения,
    проверки правильности подключения к серверу и проведения операций над разделяемым ресурсом

    Для запуска всего класса необходимо установить правильные значения в БД, в случае если необходимо наглядно проверить
    работу с разделяемым ресурсом:
    Минимальные значения для каждого продуета НЕ МЕНЬШЕ 3(можно больше),
    (на сервере стоит проверка, чтобы значение не опускалось ниже 0, в случае если оно будет ниже 0, в БД останется 0)
    1_firstProduct_3
    2_secondProduct_3
    3_thirdProduct_3
    4_fourthProduct_3
    5_fifthProduct_3
     */

class TestMultiThreadingApps {

    /*
    testOnlyOneClient - необходим для проверки подключения к серверу и
    тестирования выполнения необходимой операции с разделяемым ресурсом
    Для начала выполнения теста необходимо проверить чтобы в БД были установлены правильные начальные значения:
    1_firstProduct_1
    2_secondProduct_1
    3_thirdProduct_1
    4_fourthProduct_1
    5_fifthProduct_1

    Минимальные значения для каждого продукта НЕ МЕНЬШЕ 1 (можно больше),
    иначе не будет наглядного примера того как изменяются данные в БД
    (на сервере стоит проверка, чтобы значение не опускалось ниже 0, в случае если оно будет ниже 0, в БД останется 0)
     */
    @Test
    fun testOnlyOneClient() {
        var firstProduct = -1
        var secondProduct = -1
        var thirdProduct = -1
        var fourthProduct = -1
        var fifthProduct = -1
        val responseFromServer = Client().startClient(3345, 35001)
        if (responseFromServer != "Connection error") {
            val partsOfMessageFromServer = responseFromServer.split("|")
            firstProduct = partsOfMessageFromServer[0].toInt()
            secondProduct = partsOfMessageFromServer[1].toInt()
            thirdProduct = partsOfMessageFromServer[2].toInt()
            fourthProduct = partsOfMessageFromServer[3].toInt()
            fifthProduct = partsOfMessageFromServer[4].toInt()
        }
        val result = (firstProduct >= 0 && secondProduct >= 0 && thirdProduct >= 0 && fourthProduct >= 0 && fifthProduct >= 0)
        assertTrue(result)
    }

    private fun comparator(list: List<String>) : Boolean {
        var result = true
        for (i in list)
            if (i.toInt() < 0) result = false

        return result
    }
    /*
    testOnlyTwoClients - необходим для проверки подключения к серверу 2 клиентов и
    тестирования выполнения необходимых операций с разделяемым ресурсом
    Для начала выполнения теста необходимо проверить чтобы в БД были установлены правильные начальные значения:
    1_firstProduct_2
    2_secondProduct_2
    3_thirdProduct_2
    4_fourthProduct_2
    5_fifthProduct_2

    Минимальные значения для каждого продукта НЕ МЕНЬШЕ 2 (можно больше),
    иначе не будет наглядного примера того как изменяются данные в БД
    (на сервере стоит проверка, чтобы значение не опускалось ниже 0, в случае если оно будет ниже 0, в БД останется 0)
     */
    @Test
    fun testOnlyTwoClients() {
        val responseFromServerForFirstClient = Client().startClient(3345, 35001)
        val responseFromServerForSecondClient = Client().startClient(3345, 35002)
        var resultFirst = false
        var resultSecond = false
        if (responseFromServerForFirstClient != "Connection error") {
            val partOfMessageFromServer = responseFromServerForFirstClient.split("|")
            resultFirst = comparator(partOfMessageFromServer)
        }
        if (responseFromServerForSecondClient != "Connection error") {
            val partOfMessageFromServer = responseFromServerForSecondClient.split("|")
            resultSecond = comparator(partOfMessageFromServer)
        }
        assertTrue(resultFirst)
        assertTrue(resultSecond)
    }
}