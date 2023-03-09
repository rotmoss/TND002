package lab6;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GUI extends JFrame 
{
    private PhoneBook _phoneBook;

    private JButton _loadButton; 
    private JButton _saveButton;
    private JButton _searchButton; 
    private JButton _nextButton; 
    private JButton _addButton; 
    private JButton _deleteButton;

    private JTextField _searchField;
    private JTextField _nameField;
    private JTextField _numberField;

    private ArrayList<Person> _searchResult;
    private int _searchResultCounter;

    public GUI() 
    {
        _phoneBook = new PhoneBook();

        setTitle("Interactive phone book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFont(new Font("SansSerif", Font.PLAIN, 20));

        _loadButton = new JButton("Load phonebook");
        _saveButton = new JButton("Save phonebook");
        _searchButton = new JButton("Search");
        _nextButton = new JButton("Next");
        _addButton = new JButton("Add");
        _deleteButton = new JButton("Delete");

        _loadButton.addActionListener(new LoadButtonListener());
        _saveButton.addActionListener(new SaveButtonListener());
        _searchButton.addActionListener(new SearchButtonListener());
        _nextButton.addActionListener(new NextButtonListener());
        _addButton.addActionListener(new AddButtonListener());
        _deleteButton.addActionListener(new DeleteButtonListener());

        _searchField = new JTextField();
        _nameField = new JTextField();
        _numberField = new JTextField();

        _searchField.setText("lab6/PhoneList.txt");

        _saveButton.setEnabled(false);
        _searchButton.setEnabled(false);
        _nextButton.setEnabled(false);
        _addButton.setEnabled(false);
        _deleteButton.setEnabled(false);

        _searchResult = new ArrayList<>();
        _searchResultCounter = 0;

        createGridLayout();

        pack();
        setVisible(true);
    }

    public void createGridLayout() 
    {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
        JPanel textPanel = new JPanel(new GridLayout(3, 1));

        buttonPanel.add(_loadButton);
        buttonPanel.add(_saveButton);
        buttonPanel.add(_searchButton);
        buttonPanel.add(_nextButton);
        buttonPanel.add(_addButton);
        buttonPanel.add(_deleteButton);

        textPanel.add(_searchField);
        textPanel.add(_nameField);
        textPanel.add(_numberField);

        Container contentPane = getContentPane();
        contentPane.add(buttonPanel, BorderLayout.WEST);
        contentPane.add(textPanel, BorderLayout.CENTER);
    }

    private class LoadButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) {
            String filename = _searchField.getText();
            _searchField.setText("");
            String result = _phoneBook.load(filename);
            if (result.equals("Phone book loaded")) {
                _nameField.setText(result);
                _saveButton.setEnabled(true);
                _searchButton.setEnabled(true);
                _addButton.setEnabled(true);
                _deleteButton.setEnabled(true);
            } else {
                _nameField.setText("Try again");
            }
        }
    }

    private class SaveButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) {
            String filename = _searchField.getText();
            _searchField.setText("");
            if (filename.isEmpty()) {
                _nameField.setText("Provide a file name");
            } else {
                String result = _phoneBook.save(filename);
                _nameField.setText(result);
            }
        }
    }

    private class SearchButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            _searchResult = _phoneBook.search(_searchField.getText());
            _searchResultCounter = 0;

            if (_searchResult.isEmpty()) 
            {
                _nameField.setText("No results found");
                _numberField.setText("");
            } 
            else 
            {                
                Person firstResult = _searchResult.get(0);
                _nameField.setText(firstResult.getFullName());
                _numberField.setText(Integer.toString(firstResult.getPhoneNumber()));
                
                if (_searchResult.size() > 1) {
                    _searchResultCounter++;
                    _nextButton.setEnabled(true);
                } else {
                    _nextButton.setEnabled(false);
                }
            }
        }
    }

    private class NextButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            Person result = _searchResult.get(_searchResultCounter);
            _nameField.setText(result.getFullName());
            _numberField.setText(Integer.toString(result.getPhoneNumber()));

            if (_searchResult.size() > _searchResultCounter) {
                _searchResultCounter++;
            } else {
                _searchResultCounter = 0;
                _nextButton.setEnabled(false);
            }
        }
    }

    private class AddButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            String message = "Type in name and phone number";

            if (!_searchField.getText().equals(message)) 
            {
                _searchField.setText(message);

                _nameField.setText("");
                _nameField.setEditable(true);

                _numberField.setText("");
                _numberField.setEditable(true);
            } else 
            {
                Boolean success = _phoneBook.addPerson(
                    _nameField.getText(), 
                    Integer.parseInt(_numberField.getText())
                );

                if (success) {
                    _searchField.setText("Person added");
                } else {
                    _searchField.setText("Person could not be added");
                }

                _nameField.setText("");
                _nameField.setEditable(false);

                _numberField.setText("");
                _numberField.setEditable(false);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            _searchField.setText(
                _phoneBook.deletPerson(
                    _nameField.getText(), 
                    Integer.parseInt(_numberField.getText())
                )
            );
        }
    }
}
