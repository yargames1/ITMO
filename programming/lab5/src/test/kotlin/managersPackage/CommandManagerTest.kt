package managersPackage

import commandsPackage.Command
import commandsPackage.CommandRegistry
import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
 class CommandManagerTest {

  @Test
  fun `getCommand should execute the correct command`() {
   val mockCommand = mockk<Command>(relaxed = true)
   mockkObject(CommandRegistry)
   every { CommandRegistry.commands } returns mutableMapOf("test" to mockCommand)

   CommandManager.getCommand("test arg1 arg2")

   verify { mockCommand.execute(listOf("arg1", "arg2")) }
   unmockkAll()
  }
 }