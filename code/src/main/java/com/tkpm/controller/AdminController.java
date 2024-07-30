package com.tkpm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.tkpm.entities.Reservation;
import com.tkpm.entities.User;
import com.tkpm.service.UserService;
import com.tkpm.view.feature_view.UserManagerFeatureView;
import com.tkpm.view.feature_view.detail_view.CRUDDetailView;
import com.tkpm.view.feature_view.detail_view.UserCRUDDetailView;
import com.tkpm.view.feature_view.tabbed_controller_view.UserManagerTabbedControllerView;
import com.tkpm.view.feature_view.table.UserCRUDTableView;
import com.tkpm.view.frame.AdminMainFrame;
import com.tkpm.view.frame.BaseMainFrame;
import com.tkpm.view.frame.form.CreateAccountForm;
import com.tkpm.view.frame.form.UpdateAccountForm;

public class AdminController extends ManagerController {

	//Forms
	protected CreateAccountForm createUserForm;
	protected UpdateAccountForm updateUserForm;
	
	//Service
	protected UserService userService;
	
	public AdminController(BaseMainFrame mainFrame) {
		super(mainFrame);
		userService = UserService.INSTANCE;
	}

	@Override
	protected void initFeatures() {
		
		//Features from Customer role
		super.initFeatures();
		
		initUserManagerFeatures();
	}
	
	protected void initUserManagerFeatures() {
		UserManagerFeatureView featureView = (UserManagerFeatureView) mainFrame
				.getFeatureViews()
				.get(AdminMainFrame.USER_MANAGER_FEATURE_INDEX);
		
		UserManagerTabbedControllerView controllerView = featureView.getTabbedControllerView();
		
		initUserCRUDFeature(controllerView);
	}
	
	protected void initUserCRUDFeature(UserManagerTabbedControllerView controllerView) {
		createUserForm = new CreateAccountForm(mainFrame);
		updateUserForm = new UpdateAccountForm(mainFrame);
		createUserForm.setTitle("Tạo tài khoản");
		updateUserForm.setTitle("Cập nhật tài khoản");
		
		initUserClickRowDisplayDetail(controllerView);
		initUserCreate(controllerView);
		initUserRead(controllerView);
		initUserUpdate(controllerView);
		initUserDelete(controllerView);
	};
	
	protected void initUserClickRowDisplayDetail(UserManagerTabbedControllerView controllerView) {
		UserCRUDDetailView detail = controllerView.getUserCRUDDetailView();
		UserCRUDTableView table = controllerView.getUserCRUDTableView();
		
		table.getSelectionModel().addListSelectionListener(event -> {
			if (table.getSelectedRow() > -1) {
				User user = table.getSelectedUser();
				if (null != user) {
					detail.setDataToDetailPanel(user);
				}
			}
		});
	}
	
	protected void initUserCreate(UserManagerTabbedControllerView controllerView) {
		UserCRUDDetailView detail = controllerView.getUserCRUDDetailView();
		
		//Init "Create user" button
		detail.getButtons().get(CRUDDetailView.CREATE_BUTTON_INDEX).addActionListener(event -> {
			createUserForm.open();
		});
		
		//Init submit button of the user form
		createUserForm.getSubmitButton().addActionListener(event -> {
					
			//Validate the form
			if (createUserForm.areThereAnyEmptyStarField()) {
				createUserForm.setError(CreateAccountForm.EMPTY_FIELD_ERROR);
				return;
			}
					
			//Check if there is an user with the same name existed
			User user = createUserForm.submit();
			User testUser = userService.findUserByUsername(user.getUsername());
			if (null != testUser) {
				createUserForm.setError(CreateAccountForm.EXISTED_USERNAME_ERROR);
				return;
			}
					
			//validate success case
			user = userService.createUser(user);
					
			//Update the table view
			initUserRead(controllerView);
					
			//Close the form
			createUserForm.setError(CreateAccountForm.NO_ERROR);
			createUserForm.close();
					
		});
		
	}
	
	protected void initUserRead(UserManagerTabbedControllerView controllerView) {
		List<User> users = userService.findAllUsers();
		UserCRUDTableView table = controllerView.getUserCRUDTableView();
		
		if (null != account) {
			//Remove the current admin
			users.removeIf(user -> user.getId().equals(account.getId()));
		}
		table.setUsers(users);
		table.update();
	}
	
	protected void initUserUpdate(UserManagerTabbedControllerView controllerView) {
		UserCRUDTableView table = controllerView.getUserCRUDTableView();
		table.getActionButtons().get(UserCRUDTableView.UPDATE_BUTTON_INDEX).addActionListener(event -> {
			User user = table.getSelectedUser();
			if (null == user) {
				return;
			}
			
			user.setAccount(userService.getExactlyAccountForUser(user));
			
			updateUserForm.setModel(user);
			updateUserForm.setVisible(true);
		});
		
		updateUserForm.getSubmitButton().addActionListener(event -> {
			
			//Validate the form
			if (updateUserForm.areThereAnyEmptyStarField()) {
				updateUserForm.setError(UpdateAccountForm.EMPTY_FIELD_ERROR);
				return;
			}
			
			//Save the old model, load the reservations for it
			User model = updateUserForm.getModel();
			model = userService.getUsersWithReservations(Arrays.asList(model.getId())).get(0);
			
			//Check if there is an usernam with the same name existed 
			//	(if same name => check if the id is the same or not)
			User user = updateUserForm.submit();
			User testUser= userService.findUserByUsername(user.getUsername());
			if (null != testUser && !user.getId().equals(testUser.getId())) {
				updateUserForm.setError(UpdateAccountForm.EXISTED_USERNAME_ERROR);
				return;
			}
			
			//Setup the reservation for the new account of the user
			Set<Reservation> reservations = model.getAccount().getReservations();
			user.getAccount().setReservations(reservations);
			for (Reservation reservation: reservations) {
				reservation.setAccount(user.getAccount());
			}
			user = userService.updateUser(user);
			
			//Update the table view
			initUserRead(controllerView);
			
			//Close the form
			updateUserForm.setError(UpdateAccountForm.NO_ERROR);
			updateUserForm.close();
		});
		
	}
	
	protected void initUserDelete(UserManagerTabbedControllerView controllerView) {
		UserCRUDDetailView detail = controllerView.getUserCRUDDetailView();
		UserCRUDTableView table = controllerView.getUserCRUDTableView();
		
		//Init "Delete airport" button
		detail.getButtons().get(CRUDDetailView.DELETE_BUTTON_INDEX).addActionListener(event -> {
			
			int input = JOptionPane.showConfirmDialog(mainFrame,
	        		"Bạn có chắc chắn muốn xóa ?\nDữ liệu bị xóa sẽ không thể khôi phục lại được.",
	        		"Xóa",
	        		JOptionPane.YES_NO_OPTION);
			
			if (JOptionPane.YES_OPTION == input) {
				List<User> users = table.getSelectedUsers();
				
				//Get the id from the users
				List<Integer> ids = users
						.stream()
						.map(user -> user.getId())
						.collect(Collectors.toList());
				
				//Delete thoose airports
				userService.deleteUsers(ids);
				
				//Clear the detail view
				detail.setDataToDetailPanel(null);
				
				//Update the table view
				initUserRead(controllerView);
				
				//Success message
				JOptionPane.showMessageDialog(null, "Đã xóa thành công.");
			}
			
		});
	}
	
}
