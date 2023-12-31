package com.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.repositery.CategoryRepository;
import com.shop.repositery.ProductRepository;

@Controller
@RequestMapping("/admin/products")
@SuppressWarnings("deprecation")
public class AdminProductsController {


	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public String index(Model model, @RequestParam(value="page", required = false) Integer p) {

		int perPage = 6;
		int page = (p != null)? p : 0;

		PageRequest pageable = PageRequest.of(page, perPage);

		Page<Product> products = productRepository.findAll(pageable);
		List<Category> categories = categoryRepository.findAll();

		HashMap<Integer, String> cats = new HashMap<>();
		for(Category cat: categories) {
			cats.put(cat.getId(), cat.getName());
		}

		model.addAttribute("products", products);
		model.addAttribute("cats", cats);

		long count = productRepository.count();
		double pageCount = Math.ceil((double)count / (double)perPage);

		model.addAttribute("pageCount",(int)pageCount);
		model.addAttribute("perPage", perPage);
		model.addAttribute("count", count);
		model.addAttribute("page", page);

		return "admin/products/index";
	}

	@GetMapping("/add")
	public String add(Product product, Model model) {

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		return "admin/products/add";
	}

	@PostMapping("/add")
	public String add(@Valid Product product, BindingResult bindingResult,MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {

		List<Category> categories = categoryRepository.findAll();
		if(bindingResult.hasErrors()) {
			model.addAttribute("categories", categories);
			return "admin/products/add";
		}

		boolean fileOK = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);

		if(filename.endsWith("jpg") || filename.endsWith("png")) {
			fileOK=true;
		}

		redirectAttributes.addFlashAttribute("message", "Product added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		redirectAttributes.addFlashAttribute("product", product);


		String slug=product.getName().toLowerCase().replace(" ","-");

		Product productExists = productRepository.findBySlug(slug);

		if(!fileOK) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg pr a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		}
		else if(productExists != null) {
			redirectAttributes.addFlashAttribute("message", "product already exist, please choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		}

		else {
			product.setSlug(slug);
			product.setImage(filename);
			productRepository.save(product);

			Files.write(path, bytes);
		}
		return "redirect:/admin/products/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {

		Product product = productRepository.getOne(id);
		List<Category> categories = categoryRepository.findAll();

		model.addAttribute("product", product);
		model.addAttribute("categories", categories);

		return "admin/products/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Product product, BindingResult bindingResult,MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {

		Product currentProduct = productRepository.getOne(product.getId());

		List<Category> categories = categoryRepository.findAll();

		if(bindingResult.hasErrors()) {
			model.addAttribute("ProductName", currentProduct.getName());
			model.addAttribute("categories", categories);
			return "admin/products/edit";
		}

		boolean fileOK = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);

		if(!file.isEmpty()) {
			if(filename.endsWith("jpg") || filename.endsWith("png")) {
				fileOK=true;
			}
		}
		else {
			fileOK = true;
		}


		redirectAttributes.addFlashAttribute("message", "Product added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		redirectAttributes.addFlashAttribute("product", product);


		String slug=product.getName().toLowerCase().replace(" ","-");

		Product productExists = productRepository.findBySlugAndIdNot(slug, product.getId());

		if(!fileOK) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg pr a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("product", product);
		}
		else if(productExists != null) {
			redirectAttributes.addFlashAttribute("message", "product already exist, please choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		}

		else {
			product.setSlug(slug);

			if(!file.isEmpty()) {
				Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
				Files.delete(path2);
				product.setImage(filename);
				Files.write(path, bytes);
			}
			else {
				product.setImage(currentProduct.getImage());
			}


			productRepository.save(product);


		}
		return "redirect:/admin/products/edit/" + product.getId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

		Product product = productRepository.getOne(id);
		Product currentProduct = productRepository.getOne(product.getId());
		Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
		Files.delete(path2);

		productRepository.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "product deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/admin/products";
	}
}


