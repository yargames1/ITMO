import java.nio.ByteBuffer

data class ClientState(
    var lengthBuffer: ByteBuffer = ByteBuffer.allocate(4),
    var objectLength: Int? = null,
    var dataBuffer: ByteBuffer? = null,
    var resultToSend: ByteBuffer? = null,
    @Volatile var stage: Stage = Stage.READ
)