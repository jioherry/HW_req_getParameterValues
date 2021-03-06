package LAB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/PhotoUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
//當數據量大於fileSizeThreshold值時，內容將被寫入磁碟
//上傳過程中無論是單個文件超過maxFileSize值，或者上傳的總量大於maxRequestSize 值都會拋出IllegalStateException 異常

public class PhotoUpload extends HttpServlet {
	
	private static final long serialVersion = 2L;
	String saveDirectory = "/images_uploaded"; // 上傳檔案的目地目錄
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		System.out.println("ContentType="+ req.getContentType()); // 測試用
		
		String realPath = getServletContext().getRealPath(saveDirectory);
		System.out.println("realPath="+ realPath); // 測試用
		File fsaveDirectory = new File(realPath);
		if (!fsaveDirectory.exists())
			fsaveDirectory.mkdirs(); // ContextPath 之下,自動建立目地目錄
		
		Collection<Part> parts = req.getParts(); // Servlet3.0新增了Part介面，讓我們方便的進行檔案上傳處理
		out.write("<h2> Total parts: "+ parts.size() +"</h2>");
		
		out.println("<form action=\"" + "" + "method=\"" + "post" + "\">");
		// 秀出圖片的所有資訊
		for(Part part : parts) { // for each 迴圈
			if (getFileNameFromPart(part) != null && part.getContentType()!= null) {
				out.println("<PRE>");
				String name = part.getName();
				String filename = getFileNameFromPart(part);
				String ContentType = part.getContentType();
				long size = part.getSize();
				File f = new File(fsaveDirectory, filename);
				
				out.println("name: "+ name);
				out.println("filename: "+ filename);
				out.println("ContentType: "+ ContentType);
				out.println("Size: "+ size);
				out.println("File: "+ f);
				
				// 利用File物件,寫入目地目錄,上傳成功
				part.write(f.toString());
				
				// 測試 InputStream 與 byte[] (幫將來model的VO預作準備)
				InputStream in = part.getInputStream();
				byte[] buf = new byte[in.available()];
				in.read(buf);
				in.close();
				out.println("buffer length: "+ buf.length);
				
				// 秀圖
				out.println("<br><img src=\""+ req.getContextPath() + saveDirectory +"/"+ filename +"\">");
				out.println("<input type='checkbox'>");
				out.println();
				out.println("</PRE>");
			}
		}
		out.println("</form>");
	}

	// 取出上傳的檔案名稱 (因為API未提供method,所以必須自行撰寫)
	public String getFileNameFromPart(Part part) {		
		String header = part.getHeader("content-disposition");
		System.out.println("header="+ header); // 測試用
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() -1)).getName();
		System.out.println("filename= "+ filename); // 測試用
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}
	
}
