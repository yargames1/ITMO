package managersPackage

import commandsPackage.Command
import commandsPackage.CommandRegistry
import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
 class CommandManagerTest {

  @Test
  fun `getCommand should execute the correct command`() {
   // Мокаем команду и её выполнение
   val mockCommand = mockk<Command>(relaxed = true)
   mockkObject(CommandRegistry)
   every { CommandRegistry.commands } returns mutableMapOf("test" to mockCommand)

   // Вызов getCommand
   CommandManager.getCommand("test arg1 arg2")

   // Проверка, что execute был вызван с правильными аргументами
   verify { mockCommand.execute(listOf("arg1", "arg2")) }
   unmockkAll()
  }
 }