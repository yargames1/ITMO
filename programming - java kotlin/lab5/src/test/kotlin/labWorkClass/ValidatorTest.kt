package labWorkClass

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

 class ValidatorTest {
  @Test
  fun `enum should return true`(){
   val check = Validator.enum("eaSy")
   assertEquals(true, check)
  }
  @Test
  fun `coords should return true`(){
   val check = Validator.coords("3 4")
   assertEquals(true, check)
  }
  @Test
  fun `longMoreThanZero should return true`(){
   val check = Validator.longMoreThanZero("430")
   assertEquals(true, check)
  }
  @Test
  fun `doubleMoreThanZero should return true`(){
   val check = Validator.doubleMoreThanZero("5.3")
   assertEquals(true, check)
  }
  @Test
  fun `str should return true`(){
   val check = Validator.str("string t")
   assertEquals(true, check)
  }
  @Test
  fun `date should return true`(){
   val check = Validator.date("2006.04.13")
   assertEquals(true, check)
  }
  @Test
  fun `time should return true`(){
   val check = Validator.time("10:12")
   assertEquals(true, check)
  }

}