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
import javafx.geometry.Pos;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class UserSocialController {

	private ObservableList<UserTable> users;

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
		userNick.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.8));
		userNick.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<UserTable, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<UserTable, String> param) {
				return param.getValue().getValue().nickname;
			}
		});

		actionColumn = new JFXTreeTableColumn<UserTable, String>("");
		actionColumn.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.2));
		actionColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> null);
		actionColumn.setStyle("-fx-alignment: CENTER");

		users = FXCollections.observableArrayList();
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
		
		if(searchUsers.getText() == null) {
			loadFollowing();
		}

		setFollowColumn();
		session = GiarSession.getInstance();
		user = session.getLoggedUser();
		users.clear();

		searchedUser = User.searchUsers(searchUsers.getText());
		for(User u : searchedUser) {
			if(!u.getNickname().equals(user.getNickname())) {
				users.add(new UserTable(u.getNickname()));
			}	
		}
	}
	
	void loadFollowing() {
		GiarSession session;
		User user;
		ArrayList<User> following;
		
		setFollowingColumn();
		session = GiarSession.getInstance();
		user = session.getLoggedUser();
		users.clear();
		
		following = User.getFollowingList(user.getNickname());
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
							
						});

						socialButton.setAlignment(Pos.CENTER);
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
							unfollowBtn.setDisable(true);

						});

						wishlistBtn.setOnAction((event) -> {

						}); 

						unfollowBtn.setAlignment(Pos.CENTER);
						unfollowBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
						wishlistBtn.setAlignment(Pos.CENTER);
						wishlistBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");

						HBox buttons = new HBox(unfollowBtn, wishlistBtn);
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

	}
}
