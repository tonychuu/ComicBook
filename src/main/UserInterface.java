package main;

import comic.RequestResult;
import comic.Comic;
import comic.ComicCoverImage;
import comic.Creator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterface extends Application{
	int currpg = 1;
	@Override
    public void start(Stage primaryStage) {
		FlowPane root = new FlowPane();
		root.setVgap(10);
		root.setHgap(10);
		Backend m = new Backend();
		
		addComics(root, m, currpg);
		
		root.getChildren().add(addBackButton(root, m));
		root.getChildren().add(addPageLabel(root, m));
		root.getChildren().add(addPageField(root, m));
		root.getChildren().add(addForwardButton(root, m));
        Scene scene = new Scene(root, 880, 856); //5 column 3 rows = 15 comics

        primaryStage.setTitle("Marvel Comics");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	public static void main(String[] args) {
        launch(args);
    }
	
	/**
	 * Creates the User Interface for a particular page
	 *
	 * @param  root: the pane
	 * @param  m: An instance of the Backend class
	 * @param  wantpage: the page to retrieve
	 * @return nil
	 */
	public void addComics(Pane root, Backend m, int wantpage) {
		if (wantpage > m.getTotalPages()) {
			System.err.println("Value too large, staying in current page");
		} else if (wantpage < 1) {
			System.err.println("Value too small, staying in current page");
		} else {
			RequestResult rr = m.getPage(wantpage);
			for (int i = 0; i < rr.getData().getCount(); i++) {
				Comic comic = rr.getData().getResults(i);
				ComicCoverImage image = comic.getImage();
				String imgsrc;
				ImageView imgview;
				if (image != null) {
					imgsrc = comic.getImage().getURL();
					System.out.println("Fetching image for comic book #" + comic.getId());
					imgview = new ImageView(new Image(imgsrc));
				} else {
					imgsrc = "/noimg.jpg";
					System.out.println("No image available for comic book #" + comic.getId());
					imgview = new ImageView(new Image(getClass().getResourceAsStream(imgsrc)));
				}
				
				imgview.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						VBox infopane = new VBox();
						addText("Title: " + comic.getTitle(), infopane);
						addText("Issue Number: " + (int) comic.getIssueNum(), infopane);
						addText("Page Count: " + comic.getPageCount(), infopane);						
						addText("Format: " + comic.getFormat(), infopane);
						addText("Price: $" + comic.getPrice().getCost(), infopane);
						addText("Description: " + comic.getDescription(), infopane);
						addText("ISBN: " + comic.getISBN(), infopane);
						addText("Diamond Code: " + comic.getDiamondCode(), infopane);
						addText("EAN: " + comic.getEAN(), infopane);
						addText("UPC: " + comic.getUPC(), infopane);
						addText("ISSN: " + comic.getISSN(), infopane);	
						for (int i = 0; i < comic.getCreators().getSize(); i++) {
							Creator creator= comic.getCreators().getCreator(i);
							addText(creator.getRole() + ": " + creator.getName(), infopane);
						}
						Stage infostage = new Stage();
						Scene infoscene = new Scene(infopane, 500, 400);
						infostage.setScene(infoscene);
						infostage.setTitle(comic.getTitle());
						infostage.show();
					}
				});
				root.getChildren().add(imgview);
			}
		}
	}
	
	/**
	 * Adds a text to the pane 
	 *
	 * @param  txtline: the text to add
	 * @param  pane: the pane to add the text to
	 * @return  nil
	 */
	public void addText(String txtline, Pane pane) {
		Text text = new Text(txtline);
		text.setWrappingWidth(400);
		pane.getChildren().add(text);
	}
	
	/**
	 * Adds a back button to the pane
	 *
	 * @param  pane: the pane to add the button to
	 * @param  m: An instance of the Backend class
	 * @return      a back button
	 */
	public Button addBackButton(Pane pane, Backend m) {
		Button back = new Button("Prev. Page");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currpg > 1) {
					currpg = currpg - 1;
					pane.getChildren().clear();
					addComics(pane, m, currpg);
					TextField txtfield = addPageField(pane, m);
					Button forward = addForwardButton(pane, m);
					pane.getChildren().add(back);
					pane.getChildren().add(addPageLabel(pane, m));
					pane.getChildren().add(txtfield);
					pane.getChildren().add(forward);
				} else {
					System.err.println("Value too small. Staying on current page");
				}
			}
		});
		return back;
	}
	
	/**
	 * Adds a forward button to the pane
	 *
	 * @param  pane: the pane to add the button to
	 * @param  m: An instance of the Backend class
	 * @return      a forward button
	 */
	public Button addForwardButton(Pane pane, Backend m) {
		Button forward = new Button("Next Page (MORE COMICS!)");
		forward.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currpg + 1 > m.getTotalPages()) {
					System.err.println("No more pages. Staying on current page");
				} else {
					pane.getChildren().clear();
					currpg = currpg + 1;
					addComics(pane, m, currpg);
					TextField txtfield = addPageField(pane, m);
					Button back = addBackButton(pane, m);
					pane.getChildren().add(back);
					pane.getChildren().add(addPageLabel(pane, m));
					pane.getChildren().add(txtfield);
					pane.getChildren().add(forward);
				}
			}
		});
		return forward;
	}
	
	/**
	 * Adds a textfield that shows the current page. Users can also manually enter a specific page number to jump to
	 *
	 * @param  pane: the pane to add the textfield to
	 * @param  m:  an instance of the Backend class
	 * @return      a textfield
	 */
	public TextField addPageField(Pane pane, Backend m) {
		TextField txtfield = new TextField(Integer.toString(currpg));
		txtfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					String str = txtfield.getText();
					try {
						int entervalue = Integer.parseInt(str);
						if (entervalue > m.getTotalPages()) {
							System.err.println("Entered value is too big. Staying on current page");
							txtfield.setText(Integer.toString(currpg));
						} else if (entervalue > 0){
							pane.getChildren().clear();
							addComics(pane, m, entervalue);
							currpg = entervalue;
							Button back = addBackButton(pane, m);
							pane.getChildren().add(back);
							pane.getChildren().add(addPageLabel(pane, m));
							pane.getChildren().add(txtfield);
							Button forward = addForwardButton(pane, m);
							pane.getChildren().add(forward);
						} else {
							System.err.println("Entered value is not a valid page number. Staying on current page");
							txtfield.setText(Integer.toString(currpg));
						}
					} catch (NumberFormatException e) {
						System.err.println("Entered value is not an integer. Staying on current page");
						txtfield.setText(Integer.toString(currpg));
					}
				}
			}
		});
		return txtfield;
	}
	
	/**
	 * Adds a current page label
	 *
	 * @param  pane: the pane to add the label to
	 * @param  m: an instance of the Backend class
	 * @return      a current page label
	 */
	public Label addPageLabel(Pane pane, Backend m) {
		Label label = new Label("Current Page:");
		return label;
	}
	
}
