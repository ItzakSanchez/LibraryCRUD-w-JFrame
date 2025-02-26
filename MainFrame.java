package edgarItzak.libraryCRUD;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTable bookTable = new JTable();
	private JTextField textFieldId;
	private JTextField textFieldTitle;
	private JTextField textFieldAuthor;
	private JTextField textFieldPublishDate;
	private JTextField textFieldCategory;
	private JTextField textFieldPrice;
	private JTextField textFieldStock;
	private JTable tableSaleList;
	private JTextField textFieldBookSelectedName;
	JButton btnClearSelection = new JButton("Clear selection");
	JButton btnAddNewBook = new JButton("Add new book");
	JButton btnUpdateBook = new JButton("Update book");
	JButton btnDeleteBook = new JButton("Delete book");
	JButton btnSellBook = new JButton("Sell this book");
	DefaultTableModel tableSaleModel = new DefaultTableModel();
	JLabel lblSellOutput = new JLabel("");
	JLabel lblOperationOutput = new JLabel("");
	JLabel lblTodaysSales = new JLabel("Today's total sales: $");
	JLabel lblTotalSales = new JLabel("Total sales: $");
	DecimalFormat floatFormat = new DecimalFormat("#.00");


	
	DefaultTableModel bookModel = new DefaultTableModel();
	float todaysTotalSales = 0.0f;
	float totalSales = 0.0f;
	boolean wasSold;

	//ADD BOOK
		public void addBook(List<Book> bookList, String title, String author, String publicationDate, String category, float price,int stock) {
			int maxId = 0;
			for(Book book:bookList) {
				if(book.getId()>maxId) {
					maxId = book.getId();
				}
			}
			Book newBook = new Book((maxId+1), title, author, publicationDate, category, price, stock);
			bookList.add(newBook);
		}
		//SEARCH BY TITLE
		public List<Book> searchBookByTitle(List<Book> bookList, String title) {
			List<Book> booksByTitle = new ArrayList<Book>();
			bookList.forEach(n -> {
				if(n.getTitle().toLowerCase().contains(title.toLowerCase())) {
					booksByTitle.add(n);
				}
			});
			return booksByTitle;
		}
		//SEARCH BY AUTHOR
		public List<Book> searchBookByAuthor(List<Book> bookList, String author) {
			List<Book> booksByAuthor = new ArrayList<Book>();
			bookList.forEach(n -> {
				if(n.getAuthor().toLowerCase().contains(author.toLowerCase())) {
					booksByAuthor.add(n);
				}
			});
			return booksByAuthor;
		}
		//SEARCH BY CATEGORY
		public List<Book> searchBookByCategory(List<Book> bookList, String category) {
			List<Book> booksByCategory = new ArrayList<Book>();
			bookList.forEach(n -> {
				if(n.getCategory().toLowerCase().contains(category.toLowerCase())) {
					booksByCategory.add(n);
				}
			});
			return booksByCategory;
		}
		
		//TOTAL SALES
			public float totalSales(List<Sale> saleList) {
				float totalSales = 0.0f;
				for(Sale sale: saleList) {
					totalSales = totalSales + (sale.getPrice()*sale.getQuantity());
				}
				return totalSales;
			}
		//SELL BOOK
		public boolean sellBook(List<Sale> saleList,Book bookToSell, int cuantity){
			boolean sold = bookToSell.sellBook(cuantity);
			if (sold) {
				Sale sale = new Sale(saleList.size()+1,bookToSell.getId(), cuantity, bookToSell.getPrice(),LocalDate.now().toString());
				saleList.add(sale);
			}
			return sold;
		}
		
		
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		
		List<Book> bookList = new ArrayList<Book>();
		List<Sale> salesList = new ArrayList<Sale>();
		//Example
		Book book1 = new Book(1,"Don Quijote de la Mancha", "Miguel de Cervantes", "1797", "Parody",(float) 15.80, 20);
		Book book2 = new Book(2,"Cien años de soledad", "Gabriel García Márquez", "1982", "Magical realist",(float) 18.80, 50);
		Book book3 = new Book(3,"El Alquimista", "Paulo Cohelo", "1988", "Adventure",(float) 10.10, 35);
		
		bookList.add(book1);
		bookList.add(book2);
		bookList.add(book3);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
		
		mainPanel.setBorder(BorderFactory.createTitledBorder("Library Manage System"));
		
		
		//****************************
		//* SEARCH PANEL (BOOK LIST) *
		//****************************
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(10, 245, 548, 289);
		searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		searchPanel.setBorder(BorderFactory.createTitledBorder("Book List"));
		
		mainPanel.add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setBounds(16, 26, 50, 14);
		searchPanel.add(lblSearch);
		
		
		JComboBox comboBoxSearchType = new JComboBox();
		comboBoxSearchType.setModel(new DefaultComboBoxModel(new String[] {"by title", "by author", "by category"}));
		comboBoxSearchType.setBounds(67, 23, 84, 20);
		searchPanel.add(comboBoxSearchType);
		
		JTextField searchInput = new JTextField();
		searchInput.setColumns(10);
		searchInput.setBounds(157,23,86,20);
		searchPanel.add(searchInput);
		
		JButton btnSearchBook = new JButton("Search");
		btnSearchBook.setBounds(249, 22, 92, 23);
		btnSearchBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (searchInput.getText().length()>0) {
					int searchType =comboBoxSearchType.getSelectedIndex();
					//Title
					if (searchType == 0) {
						bookTable.clearSelection();
						bookModel.setRowCount(0);
						for(Book book: (searchBookByTitle(bookList, searchInput.getText().toString()))) {
							Object [] row = {book.getId(),book.getTitle(),book.getAuthor(),book.getPublicationDate(),book.getCategory(),book.getPrice(),book.getStock()};
							bookModel.addRow(row);
						}
						//clear textFields
						textFieldId.setText("");
						textFieldTitle.setText("");
						textFieldAuthor.setText("");
						textFieldPublishDate.setText("");
						textFieldCategory.setText("");
						textFieldPrice.setText("");
						textFieldStock.setText("");
						textFieldBookSelectedName.setText("");

						//BUTTON UPDATES
						btnClearSelection.setEnabled(true);
						btnAddNewBook.setEnabled(true);
						btnUpdateBook.setEnabled(false);
						btnDeleteBook.setEnabled(false);
						btnSellBook.setEnabled(false);
					}
					//Author
					if (searchType == 1) {
						bookTable.clearSelection();
						bookModel.setRowCount(0);
						for(Book book: (searchBookByAuthor(bookList, searchInput.getText().toString()))) {
							Object [] row = {book.getId(),book.getTitle(),book.getAuthor(),book.getPublicationDate(),book.getCategory(),book.getPrice(),book.getStock()};
							bookModel.addRow(row);
						}
						//clear textFields
						textFieldId.setText("");
						textFieldTitle.setText("");
						textFieldAuthor.setText("");
						textFieldPublishDate.setText("");
						textFieldCategory.setText("");
						textFieldPrice.setText("");
						textFieldStock.setText("");
						textFieldBookSelectedName.setText("");

						//BUTTON UPDATES
						btnClearSelection.setEnabled(true);
						btnAddNewBook.setEnabled(true);
						btnUpdateBook.setEnabled(false);
						btnDeleteBook.setEnabled(false);
						btnSellBook.setEnabled(false);
					}
					//Category
					if (searchType == 2) {
						bookTable.clearSelection();
						bookModel.setRowCount(0);
						for(Book book: (searchBookByCategory(bookList, searchInput.getText().toString()))) {
							Object [] row = {book.getId(),book.getTitle(),book.getAuthor(),book.getPublicationDate(),book.getCategory(),book.getPrice(),book.getStock()};
							bookModel.addRow(row);
						}
						//clear textFields
						textFieldId.setText("");
						textFieldTitle.setText("");
						textFieldAuthor.setText("");
						textFieldPublishDate.setText("");
						textFieldCategory.setText("");
						textFieldPrice.setText("");
						textFieldStock.setText("");
						textFieldBookSelectedName.setText("");

						//BUTTON UPDATES
						btnClearSelection.setEnabled(true);
						btnAddNewBook.setEnabled(true);
						btnUpdateBook.setEnabled(false);
						btnDeleteBook.setEnabled(false);
						btnSellBook.setEnabled(false);
					}
				}
			}
		});
		searchPanel.add(btnSearchBook);


		bookModel.addColumn("Id");
		bookModel.addColumn("Title");
		bookModel.addColumn("Author");
		bookModel.addColumn("Publish date");
		bookModel.addColumn("Category");
		bookModel.addColumn("Price");
		bookModel.addColumn("Stock");
		
		//FILL TABLE WITH BOOK LIST DATA
		bookTable.clearSelection();
		bookModel.setRowCount(0);
		for(Book book: bookList) {
			Object [] row = {book.getId(),book.getTitle(),book.getAuthor(),book.getPublicationDate(),book.getCategory(),book.getPrice(),book.getStock()};
			bookModel.addRow(row);
		}
		

		bookTable.setModel(bookModel);
		
		bookTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		bookTable.getColumnModel().getColumn(1).setPreferredWidth(137);
		bookTable.getColumnModel().getColumn(2).setPreferredWidth(123);
		bookTable.getColumnModel().getColumn(6).setPreferredWidth(45);
		//SELECT ROW. BOOK TABLE EVENT
		bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (bookTable.getSelectedRow()>=0) {
					int bookTblRow = bookTable.getSelectedRow();
					//ADD DATA TO TEXTFIELDS
					textFieldId.setText((bookTable.getValueAt(bookTblRow, 0)).toString());
					textFieldTitle.setText((bookTable.getValueAt(bookTblRow, 1)).toString());
					textFieldAuthor.setText((bookTable.getValueAt(bookTblRow, 2)).toString());
					textFieldPublishDate.setText((bookTable.getValueAt(bookTblRow, 3)).toString());
					textFieldCategory.setText((bookTable.getValueAt(bookTblRow, 4)).toString());
					textFieldPrice.setText((bookTable.getValueAt(bookTblRow, 5)).toString());
					textFieldStock.setText((bookTable.getValueAt(bookTblRow, 6)).toString());
					textFieldBookSelectedName.setText((bookTable.getValueAt(bookTblRow, 1)).toString());
					//BUTTON UPDATES
					btnClearSelection.setEnabled(true);
					btnAddNewBook.setEnabled(false);
					btnUpdateBook.setEnabled(true);
					btnDeleteBook.setEnabled(true);
					btnSellBook.setEnabled(true);

				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setBounds(10, 65, 528, 213);
		searchPanel.add(scrollPane);
		
		JButton btnClearSearch = new JButton("Clear search");
		btnClearSearch.setBounds(425, 22, 113, 23);
		btnClearSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookTable.clearSelection();
				bookModel.setRowCount(0);
				for(Book book: bookList) {
					Object [] row = {book.getId(),book.getTitle(),book.getAuthor(),book.getPublicationDate(),book.getCategory(),book.getPrice(),book.getStock()};
					bookModel.addRow(row);
					//clear textFields
					textFieldId.setText("");
					textFieldTitle.setText("");
					textFieldAuthor.setText("");
					textFieldPublishDate.setText("");
					textFieldCategory.setText("");
					textFieldPrice.setText("");
					textFieldStock.setText("");
					textFieldBookSelectedName.setText("");
					
					searchInput.setText("");

					//BUTTON UPDATES
					btnClearSelection.setEnabled(true);
					btnAddNewBook.setEnabled(true);
					btnUpdateBook.setEnabled(false);
					btnDeleteBook.setEnabled(false);
					btnSellBook.setEnabled(false);
				}
			}
		});
		searchPanel.add(btnClearSearch);
		
		//***************************
		//* BOOK DATA (TEXT FIELDS) *
		//***************************
		
		JPanel dataPanel = new JPanel();
		dataPanel.setBounds(10, 33, 234, 205);
		dataPanel.setBorder(BorderFactory.createTitledBorder("Data"));
		mainPanel.add(dataPanel);
		dataPanel.setLayout(null);
		
		JLabel lblId = new JLabel("Id");
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblId.setBounds(66, 28, 20, 14);
		dataPanel.add(lblId);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(61, 53, 25, 14);
		dataPanel.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAuthor.setBounds(47, 78, 41, 14);
		dataPanel.add(lblAuthor);
		
		JLabel lblPDate = new JLabel("Publish date");
		lblPDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPDate.setBounds(9, 103, 77, 14);
		dataPanel.add(lblPDate);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategory.setBounds(33, 125, 53, 14);
		dataPanel.add(lblCategory);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrice.setBounds(51, 150, 35, 14);
		dataPanel.add(lblPrice);
		
		JLabel lblStock = new JLabel("Stock");
		lblStock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStock.setBounds(52, 175, 35, 14);
		dataPanel.add(lblStock);
		
		textFieldId = new JTextField();
		textFieldId.setEditable(false);
		textFieldId.setBounds(100, 25, 105, 20);
		dataPanel.add(textFieldId);
		textFieldId.setColumns(10);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setBounds(100, 50, 105, 20);
		dataPanel.add(textFieldTitle);
		textFieldTitle.setColumns(10);
		
		textFieldAuthor = new JTextField();
		textFieldAuthor.setBounds(100, 75, 105, 20);
		dataPanel.add(textFieldAuthor);
		textFieldAuthor.setColumns(10);
		
		textFieldPublishDate = new JTextField();
		textFieldPublishDate.setBounds(100, 100, 105, 20);
		dataPanel.add(textFieldPublishDate);
		textFieldPublishDate.setColumns(10);
		
		textFieldCategory = new JTextField();
		textFieldCategory.setColumns(10);
		textFieldCategory.setBounds(100, 125, 105, 20);
		dataPanel.add(textFieldCategory);
		
		textFieldPrice = new JTextField();
		textFieldPrice.setColumns(10);
		textFieldPrice.setBounds(100, 150, 105, 20);
		dataPanel.add(textFieldPrice);
		
		textFieldStock = new JTextField();
		textFieldStock.setColumns(10);
		textFieldStock.setBounds(100, 175, 105, 20);
		dataPanel.add(textFieldStock);
		
		//*******************
		//* OPERATION PANEL *
		//*******************
		
		JPanel operationPanel = new JPanel();
		operationPanel.setBounds(256, 33, 146, 205);
		operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
		mainPanel.add(operationPanel);
		operationPanel.setLayout(null);
		
		
		btnClearSelection.setBounds(10, 29, 121, 23);
		btnClearSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldId.setText("");
				textFieldTitle.setText("");
				textFieldAuthor.setText("");
				textFieldPublishDate.setText("");
				textFieldCategory.setText("");
				textFieldPrice.setText("");
				textFieldStock.setText("");
				textFieldBookSelectedName.setText("");
				
				//BUTTON UPDATES
				btnClearSelection.setEnabled(true);
				btnAddNewBook.setEnabled(true);
				btnUpdateBook.setEnabled(false);
				btnDeleteBook.setEnabled(false);
				btnSellBook.setEnabled(false);
				
				//LABEL OUTPUT
				lblOperationOutput.setText("Cleared selection");
			}
		});
		operationPanel.add(btnClearSelection);
		
		
		btnAddNewBook.setBounds(10, 73, 121, 23);
		btnAddNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldId.getText().length()<=0) {
					if(textFieldTitle.getText().length()>0 && textFieldAuthor.getText().length()>0 && textFieldPublishDate.getText().length()>0 && textFieldCategory.getText().length()>0 && textFieldPrice.getText().length()>0 && textFieldStock.getText().length()>0) {
						//updateList
						addBook(bookList,textFieldTitle.getText(),textFieldAuthor.getText(),textFieldPublishDate.getText(),textFieldCategory.getText(), Float.parseFloat(floatFormat.format(Float.parseFloat(textFieldPrice.getText()))), Integer.parseInt(textFieldStock.getText()));
						//updateTable
						Book b = bookList.get(bookList.size()-1);						
						bookModel.addRow(new Object[] {b.getId(),b.getTitle(), b.getAuthor(),b.getPublicationDate(),b.getCategory(),b.getPrice(),b.getStock()});
						//clear textFields
						textFieldId.setText("");
						textFieldTitle.setText("");
						textFieldAuthor.setText("");
						textFieldPublishDate.setText("");
						textFieldCategory.setText("");
						textFieldPrice.setText("");
						textFieldStock.setText("");
						textFieldBookSelectedName.setText("");

						//BUTTON UPDATES
						btnClearSelection.setEnabled(true);
						btnAddNewBook.setEnabled(true);
						btnUpdateBook.setEnabled(false);
						btnDeleteBook.setEnabled(false);
						btnSellBook.setEnabled(false);
						//LABEL OUTPUT
						lblOperationOutput.setText("Added book");
						
					} else {
						//LABEL OUTPUT
						lblOperationOutput.setText("Pleas fill out all fields");
						
					}
				} else {
					//LABEL OUTPUT
					lblOperationOutput.setText("<html><body>Clear selection <br>to add a new book</html></body>");
				}
							}
		});
		operationPanel.add(btnAddNewBook);
		
		btnUpdateBook.setBounds(10, 102, 121, 23);
		btnUpdateBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldId.getText().length()>0) {
					if(textFieldTitle.getText().length()>0 && textFieldAuthor.getText().length()>0 && textFieldPublishDate.getText().length()>0 && textFieldCategory.getText().length()>0 && textFieldPrice.getText().length()>0 && textFieldStock.getText().length()>0) {
						//GetList where ID is textFieldId.getText()
						for(Book book:bookList) {
							if(book.getId() == Integer.parseInt(textFieldId.getText())) {
								book.setTitle(textFieldTitle.getText());
								book.setAuthor(textFieldAuthor.getText());
								book.setPublicationDate(textFieldPublishDate.getText());
								book.setCategory(textFieldCategory.getText());
								book.setPrice(Float.parseFloat(floatFormat.format(Float.parseFloat(textFieldPrice.getText()))));
								book.setStock(Integer.parseInt(textFieldStock.getText()));
							
								int bookTblRow = bookTable.getSelectedRow();
								//UPDATE BOOK TABLE
								bookTable.setValueAt(textFieldTitle.getText(), bookTblRow, 1);
								bookTable.setValueAt(textFieldAuthor.getText(), bookTblRow, 2);
								bookTable.setValueAt(textFieldPublishDate.getText(), bookTblRow, 3);
								bookTable.setValueAt(textFieldCategory.getText(), bookTblRow, 4);
								bookTable.setValueAt(Float.parseFloat(floatFormat.format(Float.parseFloat(textFieldPrice.getText()))), bookTblRow, 5);
								bookTable.setValueAt(textFieldStock.getText(), bookTblRow, 6);
								//CLEAR TEXTFIELDS
								textFieldId.setText("");
								textFieldTitle.setText("");
								textFieldAuthor.setText("");
								textFieldPublishDate.setText("");
								textFieldCategory.setText("");
								textFieldPrice.setText("");
								textFieldStock.setText("");
								textFieldBookSelectedName.setText("");
								
								//BUTTON UPDATES
								btnClearSelection.setEnabled(true);
								btnAddNewBook.setEnabled(true);
								btnUpdateBook.setEnabled(false);
								btnDeleteBook.setEnabled(false);
								btnSellBook.setEnabled(false);
								//LABEL OUTPUT
								lblOperationOutput.setText("Book Updated");
								bookTable.clearSelection();
								break;
							}
						}
					} else {
						//LABEL OUTPUT
						lblOperationOutput.setText("Pleas fill out all fields");
					}
				}
				else {
					//LABEL OUTPUT
					lblOperationOutput.setText("Please select a book in the table");
				}
			}
		});
		operationPanel.add(btnUpdateBook);
		
		btnDeleteBook.setBounds(10, 131, 121, 23);
		btnDeleteBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldId.getText().length()>0) {
					for(Book book:bookList) {
						if(book.getId() == Integer.parseInt(textFieldId.getText())) {
							//bookModel.removeRow(bookTable.getSelectedRow());
							int selectedRow = bookTable.getSelectedRow();							
							bookModel.removeRow(selectedRow);
							bookList.remove(book);
							
							textFieldId.setText("");
							textFieldTitle.setText("");
							textFieldAuthor.setText("");
							textFieldPublishDate.setText("");
							textFieldCategory.setText("");
							textFieldPrice.setText("");
							textFieldStock.setText("");
							textFieldBookSelectedName.setText("");
							
							//BUTTON UPDATES
							btnClearSelection.setEnabled(true);
							btnAddNewBook.setEnabled(true);
							btnUpdateBook.setEnabled(false);
							btnDeleteBook.setEnabled(false);
							btnSellBook.setEnabled(false);
							
							//LABEL OUTPUT
							lblOperationOutput.setText("Book Deleted");
							break;
						}
					}
				}
			}
		});
		operationPanel.add(btnDeleteBook);
		
		lblOperationOutput.setBounds(10, 157, 121, 37);
		operationPanel.add(lblOperationOutput);
		
		
		//********************
		//* SALES OPERATIONS *
		//********************
		
		
		JPanel salesPanel = new JPanel();
		salesPanel.setLayout(null);
		salesPanel.setBorder(BorderFactory.createTitledBorder("Sell book"));
		salesPanel.setBounds(412, 33, 146, 205);
		mainPanel.add(salesPanel);
		
		textFieldBookSelectedName = new JTextField();
		textFieldBookSelectedName.setBounds(10, 29, 126, 20);
		textFieldBookSelectedName.setEditable(false);
		salesPanel.add(textFieldBookSelectedName);
		textFieldBookSelectedName.setColumns(10);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(21, 71, 62, 14);
		salesPanel.add(lblQuantity);
		
		JSpinner spinnerQuantity = new JSpinner();
		spinnerQuantity.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spinnerQuantity.setBounds(93, 68, 30, 20);
		salesPanel.add(spinnerQuantity);
		
		btnSellBook.setBounds(10, 109, 126, 23);
		salesPanel.add(btnSellBook);
		btnSellBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wasSold = false;
				if(textFieldBookSelectedName.getText().length()>0 && textFieldId.getText().length()>0) {
					for(Book book:bookList) {
						if(book.getId() == Integer.parseInt(textFieldId.getText())) {
							wasSold = sellBook(salesList, book,(Integer) spinnerQuantity.getValue());
							if (wasSold) {
								//UPDATE TABLE.
								Object [] row = {salesList.size(),book.getId(),(Integer) spinnerQuantity.getValue(),book.getPrice(),LocalDate.now()};
								tableSaleModel.addRow(row);
								//UPDATE BOOK TABLE FROM BOOK LIST
								bookTable.setValueAt(book.getStock(), bookTable.getSelectedRow(),6);
								textFieldStock.setText(Integer.toString(book.getStock()));
								
								//UPDATE SALES
					        	todaysTotalSales = 0;
					        	totalSales = 0;
							    for (int i = 0; i < tableSaleList.getRowCount(); i++) {
							    	totalSales = totalSales + (Float.parseFloat(tableSaleList.getValueAt(i,3).toString())* Integer.parseInt(tableSaleList.getValueAt(i,2).toString()));
							    	totalSales =  Float.parseFloat(floatFormat.format(totalSales));
							        if(tableSaleList.getValueAt(i, 4).toString().equals(LocalDate.now().toString())) {
							        	todaysTotalSales = todaysTotalSales + (Float.parseFloat(tableSaleList.getValueAt(i,3).toString()) * Integer.parseInt(tableSaleList.getValueAt(i,2).toString()));
							        	todaysTotalSales =  Float.parseFloat(floatFormat.format(todaysTotalSales));
							        }
							    }
								//UPDATE SALES LABEL
							    lblTodaysSales.setText("Today's total sales: $"+todaysTotalSales);
							    lblTotalSales.setText("Total sales: $"+totalSales);
							
								lblSellOutput.setText("Book sold.");
							} else {
								lblSellOutput.setText("<html><body>Cannot sell book,<br> please verify stock.</body></html>");
							}
							
							break;
						}
					}
				}
			}
		});
		
		lblSellOutput.setBounds(10, 145, 126, 49);
		salesPanel.add(lblSellOutput);
		
		
		//**********************
		//* SALES LIST (TABLE) *
		//**********************
		
		JPanel saleListPanel = new JPanel();
		saleListPanel.setBounds(568, 33, 360, 369);
		saleListPanel.setBorder(BorderFactory.createTitledBorder("Sales List"));
		mainPanel.add(saleListPanel);
		saleListPanel.setLayout(null);
		
		tableSaleModel.addColumn("IDSale");
		tableSaleModel.addColumn("IdBook");
		tableSaleModel.addColumn("Quantity");
		tableSaleModel.addColumn("Price");
		tableSaleModel.addColumn("Date");
		Object [] row = {"1", "1", "1", "19.80", "2024-07-11"};
		tableSaleModel.addRow(row);

		tableSaleList = new JTable();
		tableSaleList.setModel(tableSaleModel);
		tableSaleList.getColumnModel().getColumn(0).setPreferredWidth(53);
		tableSaleList.getColumnModel().getColumn(1).setPreferredWidth(62);
		tableSaleList.getColumnModel().getColumn(2).setPreferredWidth(61);
		
		JScrollPane scrollPanelSaleList = new JScrollPane(tableSaleList);
		scrollPanelSaleList.setBounds(10, 30, 340, 328);
		saleListPanel.add(scrollPanelSaleList);
		
		
		//***************
		//* TOTAL SALES *
		//***************
		
		JPanel totalSalesPanel = new JPanel();
		totalSalesPanel.setBounds(568, 413, 360, 121);
		totalSalesPanel.setBorder(BorderFactory.createTitledBorder("Total Sales"));
		mainPanel.add(totalSalesPanel);
		totalSalesPanel.setLayout(null);
		
    	todaysTotalSales = 0;
    	totalSales = 0;
	    for (int i = 0; i < tableSaleList.getRowCount(); i++) {
	    	totalSales = totalSales + (Float.parseFloat(tableSaleList.getValueAt(i,3).toString()))*Integer.parseInt(tableSaleList.getValueAt(i,2).toString());
	        if(tableSaleList.getValueAt(i, 4).toString().equals(LocalDate.now().toString())) {
	        	todaysTotalSales = 0;
	        	totalSales = 0;
	        	todaysTotalSales = todaysTotalSales + (Float.parseFloat(tableSaleList.getValueAt(i,3).toString()))*Integer.parseInt(tableSaleList.getValueAt(i,2).toString());
	        }
	    }
		    
		lblTodaysSales.setText("Today's total sales: $"+todaysTotalSales);
		lblTodaysSales.setBounds(16, 42, 182, 45);
		totalSalesPanel.add(lblTodaysSales);
		
		lblTotalSales.setText("Total sales: $"+totalSales);
		lblTotalSales.setBounds(208, 42, 142, 45);
		totalSalesPanel.add(lblTotalSales);
	}


	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setAlwaysOnTop(false);
		frame.setBounds(10,10,1000, 600);
		frame.setTitle("Library Management System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
