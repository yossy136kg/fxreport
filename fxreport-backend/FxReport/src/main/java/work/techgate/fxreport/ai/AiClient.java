package work.techgate.fxreport.ai;

import java.io.IOException;

public interface AiClient {
	String generate(String prompt) throws IOException;
}
