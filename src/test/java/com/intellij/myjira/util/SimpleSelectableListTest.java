package com.intellij.myjira.util;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.junit.Assert;
import org.junit.Test;

public class SimpleSelectableListTest {

    @Test
    public void test_add(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();

        myList.add("Bananas");
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Bananas");

        myList.add("Apples");
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Bananas");

        myList.add("Oranges", true);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Oranges");
    }


    @Test
    public void test_update_one(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples", true);
        myList.add("Oranges");

        myList.update(1, "Apples2", false);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Bananas");
        Assert.assertEquals(myList.getItems().get(1), "Apples2");
    }

    @Test
    public void test_update_two(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples", true);
        myList.add("Oranges");

        myList.update(2, "Oranges", true);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Oranges");
    }

    @Test
    public void test_remove_case_one(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples");
        myList.add("Oranges", true);

        myList.remove(2);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Apples");
    }

    @Test
    public void test_remove_case_two(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples", true);
        myList.add("Oranges");

        myList.remove(1);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Oranges");
    }

    @Test
    public void test_remove_case_three(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples", true);
        myList.add("Oranges");

        myList.remove(0);
        Assert.assertEquals(myList.getItems().get(myList.getSelectedItemIndex()), "Apples");
    }

    @Test
    public void test_clear(){
        SimpleSelectableList<String> myList = new SimpleSelectableList<>();
        myList.add("Bananas");
        myList.add("Apples", true);
        myList.add("Oranges");

        myList.clear();
        Assert.assertEquals(myList.getSelectedItemIndex(), -1);
    }


    @Test
    public void icon_test() {
        JPanel frame = new JPanel();

        Icon loadingIcon = IconLoader.getIcon("/icons/loading.gif");
        frame.add(new JLabel("loading... ", loadingIcon, JLabel.CENTER));

        frame.setSize(400, 300);
        frame.setVisible(true);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
