/**
 * Ограничения, сформулированные исходя из задания:
 * 1. Каждая строка с ИНН имеет в длину не более 9 цифр => скорее всего подрядчик ЮЛ (но в номере всегда нет 1 знака, что странно).
 * 2. Каждая строка с ИНН содержит 3 разделителя-символа перевода на новую строку ('\n')
 * 3. Каждая цифра в ИНН занимает 3 знака в первой подстроке до первого разделителя, 3 знака во второй и т.д.
 * 4. Лидирующий ноль в результате игнорируется
 * 5. Исходя из ограничение в п.1 результат укладывается в kotlin.Int т.к. 999 999 999 < 2147483647
 *
 */
fun main() {
    //Шаг 1. Задаем шаблон для каждой цифры от 1 до 9 в представлении подрядчика
    val template =
                " _     _  _     _  _  _  _  _ \n" +
                "| |  | _| _||_||_ |_   ||_||_|\n" +
                "|_|  ||_  _|  | _||_|  ||_| _|\n"
    //Шаг 2. на основе этого представления строим словарь, связывая тем самым число с его строковым представлением,
    //причем строкове представление ввиду того что цифры разные - будет разным, значит разумно его указать в качестве ключа
    var counter = 0
    val numbers = parseRawString(template)
        .map { it to counter++ }
        .toMap()

    //Шаг 3. Парсим тестовые примеры согласно ограничениям, получаем массив цифр, склеиваем цифры и кастуем в kotlin.Int
    //печатаем результат
    //первый пример - 023056789
    val firstTestString =
                " _  _  _  _  _  _  _  _  _ \n" +
                "| | _| _|| ||_ |_   ||_||_|\n" +
                "|_||_  _||_| _||_|  ||_| _|\n"
    parseAndPrint(firstTestString, numbers, 23056789)
    //второй пример - 823856989
    val secondTestString =
                " _  _  _  _  _  _  _  _  _ \n" +
                "|_| _| _||_||_ |_ |_||_||_|\n" +
                "|_||_  _||_| _||_| _||_| _|\n"
    parseAndPrint(secondTestString, numbers, 823856989)
}

/**
 * @param rawString Строка с представлением ИНН
 * @return Массив строковых представлений отдельных чисел
 */
fun parseRawString(rawString: String): ArrayList<String> {
    val (firstLine, secondLine, thirdLine) = rawString.split("\n").filter { it.isNotEmpty() }
    val result = ArrayList<String>()
    val digit = arrayOf(
        arrayOfNulls<String>(3),
        arrayOfNulls(3),
        arrayOfNulls(3)
    )
    var innerPos=0
    for (counter in firstLine.indices) {
        if (innerPos==3 || counter==firstLine.length-1) {
            val stringDigit = (digit[0]+digit[1]+digit[2]).asList().joinToString("")
            result.add(stringDigit)
            innerPos=0
        }
        digit[0][innerPos]=firstLine[counter].toString()
        digit[1][innerPos]=secondLine[counter].toString()
        digit[2][innerPos]=thirdLine[counter].toString()
        innerPos++
    }
    return result
}

/**
 * @param testString Строка с представлением ИНН
 * @param templatesList Словарь строковых представлени и цифр
 * @param expected Число, которое ожидается получить
 */

fun parseAndPrint(testString:String,  templatesList:Map<String, Int>, expected:Int) {
    parseRawString(testString).mapNotNull { templatesList[it] }
        .toList().joinToString("")
        .toInt()
        .apply {
            println("For test string\n$testString \n value is $this. Result is ${if(this==expected) "VALID" else "INVALID"}")
        }
}