package com.tkpm.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;
import com.tkpm.service.UserService;
import com.tkpm.view.feature_view.BaseFeatureView;
import com.tkpm.view.feature_view.FlightFeatureView;
import com.tkpm.view.feature_view.FlightManagerFeatureView;
import com.tkpm.view.feature_view.ReportFeatureView;
import com.tkpm.view.feature_view.UserManagerFeatureView;
import com.tkpm.view.feature_view.header_view.BaseHeader;
import com.tkpm.view.frame.AdminMainFrame;
import com.tkpm.view.frame.BaseMainFrame;
import com.tkpm.view.frame.CustomerMainFrame;
import com.tkpm.view.frame.ManagerMainFrame;
import com.tkpm.view.frame.form.Login;
import com.tkpm.view.frame.form.Registration;

public class LoginController{

	//Forms
	private Login loginForm;
	private Registration registrationForm;
	
	//Services
	private UserService userService;

	//Controller
	private CustomerController controller;

	private void returnToLoginForm(BaseMainFrame mainFrame) {
		mainFrame.close();
		loginForm.open();
	}
	
	private void setupLogoutForCustomerController(CustomerController controller) {
		
		BaseMainFrame mainFrame = controller.getMainFrame();
		List<BaseFeatureView> featureViews = mainFrame.getFeatureViews();
		
		FlightFeatureView flightFeatureView = (FlightFeatureView) featureViews.get(0);
		List<BaseHeader> flightFeatureHeaders = flightFeatureView.getTabbedControllerView().getHeaderViews();
		for (BaseHeader header: flightFeatureHeaders) {
			header.getLogoutButton().addActionListener(event -> {
				returnToLoginForm(mainFrame);
			});
		}
	}
	
	private void setupLogoutForManagerController(CustomerController controller) {
		
		setupLogoutForCustomerController(controller);
		
		BaseMainFrame mainFrame = controller.getMainFrame();
		List<BaseFeatureView> featureViews = mainFrame.getFeatureViews();
		
		//Init logout button for flight manager feature
		FlightManagerFeatureView flightManagerFeatureView = (FlightManagerFeatureView) featureViews.get(1);
		List<BaseHeader> flightManagerFeatureHeaders = flightManagerFeatureView.getTabbedControllerView().getHeaderViews();
		for (BaseHeader header: flightManagerFeatureHeaders) {
			header.getLogoutButton().addActionListener(event -> {
				returnToLoginForm(mainFrame);
			});
		}
		
		//Init logout button for report  feature
		ReportFeatureView reportFeatureView = (ReportFeatureView) featureViews.get(2);
		List<BaseHeader> reportFeatureHeaders = reportFeatureView.getTabbedControllerView().getHeaderViews();
		for (BaseHeader header: reportFeatureHeaders) {
			header.getLogoutButton().addActionListener(event -> {
				returnToLoginForm(mainFrame);
			});
		}
	}
	
	private void setupLogoutForAdminController(CustomerController controller) {
		
		setupLogoutForManagerController(controller);
		
		BaseMainFrame mainFrame = controller.getMainFrame();
		List<BaseFeatureView> featureViews = mainFrame.getFeatureViews();
		
		//Init logout button for flight manager feature
		UserManagerFeatureView userManagerFeatureView = (UserManagerFeatureView) featureViews.get(3);
		List<BaseHeader> userManagerFeatureHeaders = userManagerFeatureView.getTabbedControllerView().getHeaderViews();
		for (BaseHeader header: userManagerFeatureHeaders) {
			header.getLogoutButton().addActionListener(event -> {
				returnToLoginForm(mainFrame);
			});
		}
		
	}
	
	private CustomerController getControllerBaseOnRole(User user) {
		if (USER_ROLE.Customer.equals(USER_ROLE.convertStringToUSER_ROLE(user.getRole()))) {
			
			controller = new CustomerController(new CustomerMainFrame());
			controller.setAccount(user.getAccount());
			setupLogoutForCustomerController(controller);
		}
		else if (USER_ROLE.Manager.equals(USER_ROLE.convertStringToUSER_ROLE(user.getRole()))) {
			
			controller = new ManagerController(new ManagerMainFrame());
			controller.setAccount(user.getAccount());
			setupLogoutForManagerController(controller);
		}
		else if (USER_ROLE.Admin.equals(USER_ROLE.convertStringToUSER_ROLE(user.getRole()))) {
			
			controller = new AdminController(new AdminMainFrame());
			controller.setAccount(user.getAccount());
			setupLogoutForAdminController(controller);
		}
		else {
			controller = null;
		}
		
		return controller;
	}
	
	public int initLoginForm() {
		
		loginForm.getSubmitButton().addActionListener(event -> {
			
			User loginUser = loginForm.submit();
			
			loginUser = userService.login(loginUser);
			
			if (null == loginUser) {
				loginForm.setError(Login.WRONG_ACCOUNT_ERROR);
				return;
			}
			
			controller = getControllerBaseOnRole(loginUser);
			if (null == controller) {
				return;
			}
			controller.run();
			
			loginForm.setError(Login.NO_ERROR);
			loginForm.close();
			
		});
		
		loginForm.getRegistrateButton().addActionListener(event -> {
			registrationForm.open();
		});
		
		return 0;
	}
	
	
	public int initRegistrationForm() {
		
		registrationForm.getSubmitButton().addActionListener(event -> {
			
			if (registrationForm.areThereAnyEmptyStarField()) {
				registrationForm.setError(Registration.EMPTY_STAR_FIELD_ERROR);
				return;
			}
			
			if (registrationForm.isPasswordMismatch()) {
				registrationForm.setError(Registration.PASSWORD_MISMATCH_ERROOR);
				return;
			}
			
			User user = registrationForm.submit();
			User other = userService.findUserByUsername(user.getUsername());
			
			//Check if the username is existed
			if (null != other) {
				registrationForm.setError(Registration.EXISTED_USERNAME_ERROR);
				return;
			}
			
			userService.createUser(user);
			
			registrationForm.setError(Registration.NO_ERROR);
			registrationForm.close();
			
			
			JOptionPane.showMessageDialog(loginForm, "Đã đăng ký thành công");
		});
		
		return 0;
	}
	
	public LoginController() {
		
		this.loginForm = new Login();
		this.loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.registrationForm = new Registration(loginForm);
		this.userService = UserService.INSTANCE;
		
		initLoginForm();
		initRegistrationForm();
		
	}


	public void run() {
		loginForm.open();
	}
	
}
