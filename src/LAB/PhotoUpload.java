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
//��ƾڶq�j��fileSizeThreshold�ȮɡA���e�N�Q�g�J�Ϻ�
//�W�ǹL�{���L�׬O��Ӥ��W�LmaxFileSize�ȡA�Ϊ̤W�Ǫ��`�q�j��maxRequestSize �ȳ��|�ߥXIllegalStateException ���`

public class PhotoUpload extends HttpServlet {
	
	private static final long serialVersion = 2L;
	String saveDirectory = "/images_uploaded"; // �W���ɮת��ئa�ؿ�
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		System.out.println("ContentType="+ req.getContentType()); // ���ե�
		
		String realPath = getServletContext().getRealPath(saveDirectory);
		System.out.println("realPath="+ realPath); // ���ե�
		File fsaveDirectory = new File(realPath);
		if (!fsaveDirectory.exists())
			fsaveDirectory.mkdirs(); // ContextPath ���U,�۰ʫإߥئa�ؿ�
		
		Collection<Part> parts = req.getParts(); // Servlet3.0�s�W�FPart�����A���ڭ̤�K���i���ɮפW�ǳB�z
		out.write("<h2> Total parts: "+ parts.size() +"</h2>");
		
		out.println("<form action=\"" + "" + "method=\"" + "post" + "\">");
		// �q�X�Ϥ����Ҧ���T
		for(Part part : parts) { // for each �j��
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
				
				// �Q��File����,�g�J�ئa�ؿ�,�W�Ǧ��\
				part.write(f.toString());
				
				// ���� InputStream �P byte[] (���N��model��VO�w�@�ǳ�)
				InputStream in = part.getInputStream();
				byte[] buf = new byte[in.available()];
				in.read(buf);
				in.close();
				out.println("buffer length: "+ buf.length);
				
				// �q��
				out.println("<br><img src=\""+ req.getContextPath() + saveDirectory +"/"+ filename +"\">");
				out.println("<input type='checkbox'>");
				out.println();
				out.println("</PRE>");
			}
		}
		out.println("</form>");
	}

	// ���X�W�Ǫ��ɮצW�� (�]��API������method,�ҥH�����ۦ漶�g)
	public String getFileNameFromPart(Part part) {		
		String header = part.getHeader("content-disposition");
		System.out.println("header="+ header); // ���ե�
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() -1)).getName();
		System.out.println("filename= "+ filename); // ���ե�
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}
	
}
