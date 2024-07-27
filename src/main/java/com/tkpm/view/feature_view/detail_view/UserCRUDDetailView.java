package com.tkpm.view.feature_view.detail_view;

import com.tkpm.entities.Flight;
import com.tkpm.entities.User;

@SuppressWarnings("serial")
public class UserCRUDDetailView extends CRUDDetailView {
	
	private static final String[] FIELD_NAMES = {
			"Mã người dùng",	
			"Tên đăng nhập",
			"Chức vụ",
	};
	
	public static final String TITLE = "Tài khoản";
	
	public UserCRUDDetailView() {
		super(TITLE, FIELD_NAMES);
	}
	

	@Override
	public void setDataToDetailPanel(Object object) {
		super.setDataToDetailPanel(object);
		if (null == object) {return;}
		User user= (User) object;
		attributeData.get(0).setText(user.getId().toString());
		attributeData.get(1).setText(user.getUsername());		
		attributeData.get(2).setText(user.getRole());	
	}
}