package labWorkClass

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

 class ValidatorTest {
  @Test
  fun `enum should return true`(){
   val check = Validator.enum("eaSy")
   assertEquals(true, check)
  }

}