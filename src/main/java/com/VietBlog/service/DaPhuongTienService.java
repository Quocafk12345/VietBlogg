package com.VietBlog.service;

import com.VietBlog.entity.BaiViet;
import com.VietBlog.entity.DaPhuongTien;
import com.VietBlog.repository.BaiVietRepository;
import com.VietBlog.repository.DaPhuongTienRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DaPhuongTienService {

	private final DaPhuongTienRepository daPhuongTienRepository;
	private final Cloudinary cloudinary;
	private final BaiVietRepository baiVietRepository;

	@Autowired
	public DaPhuongTienService(DaPhuongTienRepository daPhuongTienRepository, Cloudinary cloudinary, BaiVietRepository baiVietRepository) {
		this.daPhuongTienRepository = daPhuongTienRepository;
		this.cloudinary = cloudinary;
		this.baiVietRepository = baiVietRepository;
	}

	public List<DaPhuongTien> layDaPhuongTienTheoBaiViet(Long idBaiViet) {
		return daPhuongTienRepository.findByBaiViet_Id(idBaiViet);
	}

	public DaPhuongTien layDaPhuongTienTheoDuongDan(String duongDan) {
		return daPhuongTienRepository.findByDuongDan(duongDan);
	}

	@Transactional
	public DaPhuongTien themDaPhuongTienChoBaiViet(Long baiVietId, MultipartFile filePhuongTien) throws IOException {
		Map uploadResult = cloudinary.uploader().upload(filePhuongTien.getBytes(), ObjectUtils.emptyMap());
		String duongDan = (String) uploadResult.get("secure_url");
		DaPhuongTien daPhuongTien = new DaPhuongTien();
		daPhuongTien.setDuongDan(duongDan);
		Optional<BaiViet> baiViet = baiVietRepository.findById(baiVietId);
		daPhuongTien.setBaiViet(baiViet.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết có ID: " + baiVietId)));
		daPhuongTienRepository.save(daPhuongTien);
		return daPhuongTien;
	}

	@Transactional
	public DaPhuongTien capNhatDaPhuongTien(DaPhuongTien daPhuongTien, MultipartFile filePhuongTien) {
		try {
			String duongDanCu = daPhuongTien.getDuongDan();

			Map uploadResult = cloudinary.uploader().upload(filePhuongTien.getBytes(), ObjectUtils.emptyMap());
			String duongDanMoi = (String) uploadResult.get("secure_url");

			daPhuongTien.setDuongDan(duongDanMoi);
			daPhuongTienRepository.save(daPhuongTien);

			// Xóa file cũ trên Cloudinary (nếu có)
			if (duongDanCu != null && !duongDanCu.isEmpty()) {
				xoaFileTrenCloudinary(duongDanCu);
			}
			return daPhuongTien;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public void xoaDaPhuongTien(DaPhuongTien daPhuongTien) throws IOException {
		String duongDan = daPhuongTien.getDuongDan();
		daPhuongTienRepository.delete(daPhuongTien);
		// Xóa file trên Cloudinary
		if (duongDan != null && !duongDan.isEmpty()) {
			xoaFileTrenCloudinary(duongDan);
		}
	}

	private void xoaFileTrenCloudinary(String url) throws IOException {
		// Tách public ID từ URL
		String publicId = getPublicId(url);

		// Xóa file bằng API của Cloudinary
		Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

		// Kiểm tra kết quả xóa
		if (!"ok".equals(result.get("result"))) {
			// Xử lý lỗi khi xóa file
			// Ví dụ: log lỗi hoặc throw exception
			System.err.println("Lỗi khi xóa file trên Cloudinary: " + result.get("result"));
		}
	}

	private String getPublicId(String url) {
		// Ví dụ: url = "https://res.cloudinary.com/your_cloud_name/image/upload/v1670477827/abc.jpg"
		// publicId = "image/upload/v1670477827/abc"

		try {
			// Sử dụng URL object để parse URL
			URL parsedUrl = new URL(url);

			// Tách path và loại bỏ phần mở rộng file
			String path = parsedUrl.getPath();
			String publicId = path.substring(path.indexOf("/", 1) + 1, path.lastIndexOf("."));

			return publicId;
		} catch (Exception e) {
			// Xử lý lỗi khi không thể parse URL
			e.printStackTrace();
			return null;
		}
	}

	public int countDaPhuongTienByBaiVietId(Long idBaiViet) {
		return daPhuongTienRepository.countByBaiViet_Id(idBaiViet);
	}

	public void deleteAllDaPhuongTienByBaiVietId(Long idBaiViet) {
		daPhuongTienRepository.deleteAllByBaiViet_Id(idBaiViet);
	}

}