package uet.jcia.shop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uet.jcia.shop.model.Category;
import uet.jcia.shop.model.CategoryManager;
import uet.jcia.shop.model.Item;
import uet.jcia.shop.model.Product;
import uet.jcia.shop.model.ProductManager;

/**
 * Servlet implementation class ProductService
 */
@WebServlet("/ProductService")
public class ProductService extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ProductManager pm;
    
    public ProductService() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	pm = new ProductManager();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		if (action == null) {
			return ;
		}
		
		String destination = null;
		
		// get products by category id
		if (action.equalsIgnoreCase("gpbycid")) {
			destination = "/category.jsp";
			
			try {
				String categoryIdStr = req.getParameter("categoryid");
				int categoryId = Integer.parseInt(categoryIdStr);
				List<Product> products = pm.getAllProductByCategoryId(categoryId);
				
				CategoryManager categoryManager = new CategoryManager();
				Category category = (Category) categoryManager.getItemById(categoryId);
				String categoryName = category.getName();
				
				req.setAttribute("categoryName", categoryName);
				req.setAttribute("products", products);
				
			} catch (Exception e) {
				req.setAttribute("message", "Cannot get products");
				forwardStream(req, rsp, destination);
				return ;
				
			}
			
		}
		// get product by id
		else if (action.equalsIgnoreCase("gpbyid")) {
			destination = "/product.jsp";
			
			try {
				String pIdStr = req.getParameter("productid");
				int pId = Integer.parseInt(pIdStr);
				Product p = (Product) pm.getItemById(pId);
				
				req.setAttribute("product", p);
				
			} catch (Exception e) {
				req.setAttribute("message", "Cannot get product");
				forwardStream(req, rsp, destination);
				return ;
				
			}
		}
		// search product by name
		else if (action.equalsIgnoreCase("spbyn")) {
			String input = req.getParameter("productname");
			destination = "/search.jsp";
			
			if (input.isEmpty()) {
				req.setAttribute("message", "you can not leave input empty");
			
			} else {
				List<Item> products = pm.searchItemByName(input);
				req.setAttribute("products", products);
			}
			
			req.setAttribute("productname", input);
		}
		
		forwardStream(req, rsp, destination);
	}
	
	private void forwardStream(HttpServletRequest req, HttpServletResponse rsp, String destination)
			throws ServletException, IOException {
		
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(destination);
		dispatcher.forward(req, rsp);
	}
}
