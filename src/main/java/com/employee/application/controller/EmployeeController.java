package com.employee.application.controller;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.employee.application.model.Employee;

//This line declares the EmployeeController class as a Spring MVC controller.
@Controller
public class EmployeeController {
	
	//This line autowires a DataSource object, which is a Spring bean that provides a connection to the database.
	@Autowired
    private DataSource dataSource;
	
	//This method handles the GET request for the root URL ("/") and returns the name of the view template to render, which is "index".
	@GetMapping("/")
	public String showHome(Model model) {
		return "index";
	}
	
	/*
	 * This method handles the GET request for the "/registration_form" URL and prepares a new Employee object to be bound to the form. 
	 * It adds this object to the model and returns the name of the view template to render, which is "registration_form".
	 */
	@GetMapping("/registration_form")
	public String showRegistrationForm(Model model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "registration_form";
	}
	
	@GetMapping("/delete_form")
	public String showDeleteForm() {
	    return "delete_form";
	}
	
	@GetMapping("/search_form")
    public String showSearchForm() {
        return "search_form";
    }
	
	@GetMapping("/update_id_form")
    public String showUpdateForm() {
        return "update_id_form";
    }


	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute Employee employee, Model model) {		
		
		try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO employee (id, name, age, gender, joining_date, retiring_date, dept) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	stmt.setInt(1, employee.getId());
                stmt.setString(2, employee.getName());
                stmt.setInt(3, employee.getAge());
                stmt.setString(4, employee.getGender());
                stmt.setObject(5, employee.getJoiningDate());
                stmt.setObject(6, employee.getRetiringDate());
                stmt.setString(7, employee.getDept());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }
        model.addAttribute("employee", employee);
        return "registration_success";
	}

	
    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam("id") int id, Model model) {        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM employee WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate(); // Execute the delete operation
                if (rowsAffected > 0) {
                    // If deletion is successful, return a success message
                    model.addAttribute("message", "Employee with ID " + id + " has been deleted successfully.");
                } else {
                    // If no employee with the given ID is found, return an error message
                    model.addAttribute("error", "No employee found with ID " + id);
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while deleting employee with ID " + id);
        }
        
        return "delete_result";
    }
    

    @PostMapping("/search")
    public String searchEmployeeById(@RequestParam("id") int id, Model model) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Employee employee = new Employee();
                        employee.setId(rs.getInt("id"));
                        employee.setName(rs.getString("name"));
                        employee.setAge(rs.getInt("age"));
                        employee.setGender(rs.getString("gender"));
                        // Convert SQL Date to LocalDate
                        Date joiningDate = rs.getDate("joining_date");
                        employee.setJoiningDate(joiningDate != null ? joiningDate.toLocalDate() : null);
                        Date retiringDate = rs.getDate("retiring_date");
                        employee.setRetiringDate(retiringDate != null ? retiringDate.toLocalDate() : null);
                        employee.setDept(rs.getString("dept"));
                        model.addAttribute("employee", employee);
                        return "search_result"; // Change to search_results.html
                    } else {
                        model.addAttribute("message", "Employee with ID " + id + " not found.");
                        return "search_form"; // Change to search_form.html
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            model.addAttribute("message", "An error occurred while searching for employee.");
            return "search_form"; // Change to search_form.html
        }
    }
    
    @GetMapping("/update_details_form")
    public String showUpdateDetailsForm(@RequestParam("id") int id, Model model) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Employee employee = new Employee();
                        employee.setId(rs.getInt("id"));
                        employee.setName(rs.getString("name"));
                        employee.setAge(rs.getInt("age"));
                        employee.setGender(rs.getString("gender"));
                        Date joiningDate = rs.getDate("joining_date");
                        employee.setJoiningDate(joiningDate != null ? joiningDate.toLocalDate() : null);
                        Date retiringDate = rs.getDate("retiring_date");
                        employee.setRetiringDate(retiringDate != null ? retiringDate.toLocalDate() : null);
                        employee.setDept(rs.getString("dept"));
                        model.addAttribute("employee", employee);
                        return "update_details_form";
                    } else {
                        model.addAttribute("error", "Employee with ID " + id + " not found.");
                        return "update_result";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while updating employee with ID " + id);
            return "update_result";
        }
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee, Model model) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE employee SET name=?, age=?, gender=?, joining_date=?, retiring_date=?, dept=? WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, employee.getName());
                stmt.setInt(2, employee.getAge());
                stmt.setString(3, employee.getGender());
                stmt.setObject(4, employee.getJoiningDate());
                stmt.setObject(5, employee.getRetiringDate());
                stmt.setString(6, employee.getDept());
                stmt.setInt(7, employee.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    model.addAttribute("message", "Employee with ID " + employee.getId() + " has been updated successfully.");
                } else {
                    model.addAttribute("error", "No employee found with ID " + employee.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while updating employee with ID " + employee.getId());
        }
        return "update_result";
    }
   
    
}