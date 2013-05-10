import java.io.BufferedReader;
import java.io.IOException;   
public class LineReader {   
  private boolean doDiscardNl;
  private BufferedReader br;
  public LineReader(BufferedReader br) { this.br = br; }
  public String readLine() throws IOException {
    StringBuilder sb = new StringBuilder();
    int i;
    while (0 <= (i = br.read())) {
      if (i == '\n') {
        if (doDiscardNl) {
          doDiscardNl = false;  
        } else {
          sb.append((char)'\n');
          break;
        }
      } else {
        doDiscardNl = false;
        sb.append((char)i);  
        if (i == '\r') {
          doDiscardNl = true;
          break;
        }
      }
    }
    return sb.toString();
  }
}