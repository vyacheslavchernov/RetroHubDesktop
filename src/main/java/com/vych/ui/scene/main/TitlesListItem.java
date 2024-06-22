package com.vych.ui.scene.main;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.FileInputStream;

/**
 * Item that can be contained in {@link MainSceneController} titlesListView
 */
@Getter
public class TitlesListItem extends HBox {
    private final String titleName;

    private final String titleId;

    @SneakyThrows
    public TitlesListItem(String titleName, String titleId, String cover) {
        super();

        this.titleName = titleName;
        this.titleId = titleId;

        setSpacing(32);
        alignmentProperty().set(Pos.CENTER_LEFT);

        ImageView coverView = new ImageView(new Image(new FileInputStream(cover)));
        coverView.setFitHeight(16);
        coverView.setFitWidth(16);
        getChildren().add(coverView);

        getChildren().add(new Label(titleName));
    }
}
