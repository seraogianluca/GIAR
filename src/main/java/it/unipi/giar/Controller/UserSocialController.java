package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import it.unipi.giar.GiarSession;
import it.unipi.giar.Data.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class UserSocialController {

	private ObservableList<UserTable> users;
	private ObservableList<UserTable> following;

	@FXML
	private AnchorPane socialContent;

	@FXML
	private JFXTreeTableView<UserTable> usersTable;
	private JFXTreeTableColumn<UserTable, String> userNick;
	private JFXTreeTableColumn<UserTable, String> actionColumn;

	@FXML
	private JFXTextField searchUsers;

	public void initialize() {
		userNick = new JFXTreeTableColumn<UserTable, String>("Nickname"); 
		userNick.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.6));
		userNick.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserTable, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserTable, String> param) {
				return param.getValue().getValue().nickname;
			}
		});

		actionColumn = new JFXTreeTableColumn<UserTable, String>("");
		actionColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.4));
		actionColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> null);	

		users = FXCollections.observableArrayList();
		following = FXCollections.observableArrayList();
		loadFollowing();

		final TreeItem<UserTable> root = new RecursiveTreeItem<UserTable>(users, RecursiveTreeObject::getChildren);
		usersTable.getColumns().setAll(userNick, actionColumn);
		usersTable.setRoot(root);
		usersTable.setShowRoot(false);
	}

	@FXML
	void searchUsers(KeyEvent event) {
		GiarSession session;
		User user;
		ArrayList<User> searchedUser;
		
		if(searchUsers.getText().equals("")) {
			loadFollowing();
		} else {
			setFollowColumn();
			session = GiarSession.getInstance();
			user = session.getLoggedUser();
			users.clear();

			searchedUser = User.searchUsers(searchUsers.getText());
			for(User u : searchedUser) {
				if(!u.getNickname().equals(user.getNickname())) {
					UserTable userFound = new UserTable(u.getNickname());
					if (!following.contains(userFound)) {
						users.add(userFound);
					}
				}	
			}
		}
	}
	
	private void loadFollowing() {
		GiarSession session;
		User user;
		ArrayList<User> follow;
		
		setFollowingColumn();
		session = GiarSession.getInstance();
		user = session.getLoggedUser();
		users.clear();
		following.clear();
		
		follow = User.getFollowingList(user.getNickname());
		for(User u : follow) {
			if(!u.getNickname().equals(user.getNickname())) {
				UserTable userToAdd = new UserTable(u.getNickname());
				users.add(userToAdd);
				following.add(userToAdd);
			}	
		}
	}

	private void setFollowColumn() {
		actionColumn.setCellFactory(column -> {
			TreeTableCell<UserTable, String> cell = new TreeTableCell<UserTable, String>(){
				final JFXButton socialButton = new JFXButton("Follow");

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					setText(null);

					if(empty) {
						setGraphic(null);
					} else {
						socialButton.setOnAction((event) -> {
							GiarSession session;
							User loggedUser;
							String toFollow;

							session = GiarSession.getInstance();
							loggedUser = session.getLoggedUser();
							toFollow = getTreeTableView().getTreeItem(getIndex()).getValue().nickname.get();

							User.followUser(loggedUser.getNickname(), toFollow);
							socialButton.setDisable(true);
							following.add(new UserTable(toFollow));
							loadFollowing();
						});

						socialButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
						setGraphic(socialButton);
					}
				}
			};

			return cell;
		});
	}

	void setFollowingColumn() {
		actionColumn.setCellFactory(column -> {
			TreeTableCell<UserTable, String> cell = new TreeTableCell<UserTable, String>(){
				final JFXButton unfollowBtn = new JFXButton("Unfollow");
				final JFXButton wishlistBtn = new JFXButton("Whishlist");

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					setText(null);

					if(empty) {
						setGraphic(null);
					} else {
						unfollowBtn.setOnAction((event) -> {
							GiarSession session;
							User loggedUser;
							String toUnfollow;

							session = GiarSession.getInstance();
							loggedUser = session.getLoggedUser();
							toUnfollow = getTreeTableView().getTreeItem(getIndex()).getValue().nickname.get();

							User.unfollowUser(loggedUser.getNickname(), toUnfollow);
							loadFollowing();
						});

						wishlistBtn.setOnAction((event) -> {
							try { 		
					    		String friendNickname;
					    		FXMLLoader loader;
					    		UserSocialWishlistController controller;
					    		Scene scene;
					    		AnchorPane pane;
					    		AnchorPane newPane;
					    		
					    		scene = socialContent.getScene();
					    		pane = (AnchorPane)scene.lookup("#anchorPaneRight");
					    	    		
					    		loader = new FXMLLoader();
					            loader.setLocation(getClass().getResource("/fxml/UserSocialWishlist.fxml"));
					            newPane = loader.load();
					    		
					            friendNickname = getTreeTableView().getTreeItem(getIndex()).getValue().nickname.get();
					    	    controller = loader.getController();            
					    	    controller.initialize(friendNickname);
					            
					            pane.getChildren().setAll(newPane);      

							} catch (Exception e) {
								e.printStackTrace();
							}
						}); 

						unfollowBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
						wishlistBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");

						HBox buttons = new HBox(unfollowBtn, wishlistBtn);
						buttons.setSpacing(20);
						setGraphic(buttons);
					}
				}
			};

			return cell;
		});	  
	}
	
	class UserTable extends RecursiveTreeObject<UserTable> {

		StringProperty nickname;

		public UserTable(String name) {
			this.nickname = new SimpleStringProperty(name);
		}
		
		@Override
		public boolean equals(Object o) {
			
	        if (o == this) { 
	            return true; 
	        } 
	  
	        if (!(o instanceof UserTable)) { 
	            return false; 
	        } 
	          
	        UserTable u = (UserTable)o; 
	           
	        return this.nickname.get().equals(u.nickname.get()); 
		}

	}
}
