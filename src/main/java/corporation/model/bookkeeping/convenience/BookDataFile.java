package corporation.model.bookkeeping.convenience;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Column;

@Entity
public class BookDataFile implements Serializable {
	
	private Long id;
	
	private byte[] fileData;
	private String encoding;
	private String script;
	private Date uploaded;
	
	public BookDataFile() {
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	@Lob
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	@Column(length=10000)
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	public Date getUploaded() {
		return uploaded;
	}
	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}
	
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public String decode() throws IOException {
		Charset charset = null;
		if (getEncoding() != null) 
			charset = Charset.forName(getEncoding());
		else charset = Charset.forName("ISO-8859-1");
		
		// dekodar enligt charset
		Reader reader = new InputStreamReader(new ByteArrayInputStream(getFileData()), charset);
		StringBuffer buf = new StringBuffer();
		do {
			int c = reader.read();
			if (c == -1)
				break;
			buf.append((char)c);
		} while(true);
		return buf.toString();
	}
	
}
