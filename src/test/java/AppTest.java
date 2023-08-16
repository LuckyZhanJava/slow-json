import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;

public class AppTest {

  public static void main(String[] args) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaType type = null;
    LocalDateTime now = objectMapper.readValue(new byte[0], type);
  }
}
