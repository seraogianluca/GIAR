package it.unipi.giar.Controller;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

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
import javafx.util.Callback;

public class UserSocialController {

	private User user;
	
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
    	//FIXME: Class fields must be private because is readonly, is better to use get methods for access.
    	this.user = UserMenuController.user;
    	
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
         
        //TODO(GIANLUCA): Fill with the following.
        users = FXCollections.observableArrayList();
  
        final TreeItem<UserTable> root = new RecursiveTreeItem<UserTable>(users, RecursiveTreeObject::getChildren);
        usersTable.getColumns().setAll(userNick, actionColumn);
        usersTable.setRoot(root);
        usersTable.setShowRoot(false);
    }
    
    void loadFollowing() {
    	 actionColumn.setCellFactory(column -> {
         	TreeTableCell<UserTable, String> cell = new TreeTableCell<UserTable, String>(){
         		final JFXButton unfollowBtn = new JFXButton("Unfollow");

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
         				
         				unfollowBtn.setText("Unfollow");
         				unfollowBtn.setAlignment(Pos.CENTER);
         				unfollowBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
         				setGraphic(unfollowBtn);
         			}
         		}
         	};
         	
         	return cell;
         });	 
    	 
    	 
    }
    
    @FXML
    void searchUsers(KeyEvent event) {
    	actionColumn.setCellFactory(column -> {
         	TreeTableCell<UserTable, String> cell = new TreeTableCell<UserTable, String>(){
         		final JFXButton socialButton = new JFXButton();

         		@Override
         		protected void updateItem(String item, boolean empty) {
         			super.updateItem(item, empty);
         			setText(null);

         			if(empty) {
         				setGraphic(null);
         			} else {
         				socialButton.setOnAction((event) -> {
         					socialButton.setDisable(true);
         					//TODO Follow
         					String nickname = getTreeTableView().getTreeItem(getIndex()).getValue().nickname.get();
         					//User.followUser(nickname);
         				});
         				
         				socialButton.setText("Follow");
         				socialButton.setAlignment(Pos.CENTER);
         				socialButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
         				setGraphic(socialButton);
         			}
         		}
         	};
         	
         	return cell;
         });
    	
    	users.clear();
    	ArrayList<User> userSearch = User.searchUsers(searchUsers.getText());
    	for(User u : userSearch) {
    		if(!u.getNickname().equals(user.getNickname())) {
    			users.add(new UserTable(u.getNickname()));
    		}	
    	}
    }
    
    class UserTable extends RecursiveTreeObject<UserTable> {

        StringProperty nickname;

        public UserTable(String name) {
            this.nickname = new SimpleStringProperty(name);
        }

    }
}
