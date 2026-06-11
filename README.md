# Open Way Technical Test: QA Automation

This repository contains the automated UI test for the Periplus shopping cart feature, implemented using Java, Selenium WebDriver, and TestNG.

## How to Run the Tests

1. **Set up Credentials:** Create a new file named `.env` in the root directory. Copy the structure from `.env.example` and input your Periplus test account credentials:
   ```env
   PERIPLUS_EMAIL=your_email@domain.com
   PERIPLUS_PASSWORD=your_password
   ```
2. **Execute the Test:**
   Because this project uses Maven (`pom.xml`) for dependency management, you do not need to manually configure `.jar` files. Simply open the project in **IntelliJ IDEA**, let Maven download the dependencies, and run the `PeriplusCartTest` class directly.

---

## Approach & Design Decisions

* **Authentication as a Precondition:** Since the primary goal is testing the cart functionality, the login process is handled in the setup phase (`@BeforeClass`) rather than as part of the core test logic.
- **Clear Cart Precondition:** I added a step to clear the cart before the test begins, even though it was not explicitly instructed. This is to ensure test idempotency. A shopping cart can have a limit on the number of items it can contain. If we run the same test enough times, the cart will eventually become full, and the test will fail because of a data issue, not because of the feature implementation itself (a false negative).
    - *Alternative Considered:* The other alternative I thought about was to keep adding new products without resetting the cart. However, that means we would need to handle validation for several different cases: when we add an item to a cart that is empty, when we add a book that already exists in the cart, and when we add a new book that doesn't exist in the cart yet. We would also need to handle the scenario when the cart is completely full (either by clearing it at that moment or printing a warning message). This creates unnecessary complexity, which is why I opted for a clean reset instead.

- **Search with a General Query:** Because we need to find a book to add to the cart, another precondition is that there must exist a book with a stock > 0. There are two approaches that came to mind to ensure this:
    1. *Search for a general query (Currently Implemented):* If we search for a specific book title, or even specific genres, there is a possibility that the specific book becomes out of stock in the future or that the book will not be sold anymore. This would result in the search step not returning any books, and the test would fail because of a data issue. When we use a general query, such as "book", the search engine is practically guaranteed to return available books.
    2. *Directly fetch a book from the home page without using a search query (Alternative Considered):* Take Amazon as an example. Their homepage doesn't showcase individual items directly; instead, it shows groups of products, and we need to click those groups to find an individual item showcase. I think it is very probable that this design choice could be updated on Periplus in the future to change how they currently showcase individual books. If they change that layout, this approach would make the test fail. Therefore, the general search approach is safer.

- **Page Object Model (POM) Design Pattern:** I implemented this because it is the best practice for Selenium. When the UI changes, we only need to change the locators in the specific page class, not the core test logic.