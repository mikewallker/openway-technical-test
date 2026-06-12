## Core Test Cases

The following five test cases represent the most critical user journeys and are written in full verbose format to demonstrate the standard test case structure in practice.

---

## Test Case ID: TC-CART-001

### Title
Verify a logged-in user can add a standard in-stock item to the shopping cart.

### Preconditions
- The user is authenticated with a registered account.
- The shopping cart is empty.
- At least one product is available on the platform with stock > 0.

### Test Steps
1. Navigate to the platform homepage.
2. Search for a product (e.g., type "book" in the search bar).
3. Click on the desired product from the search results to open its Product Detail Page (PDP).
4. Click the **Add to Cart** button.
5. Navigate to the Shopping Cart page.

### Expected Result
- The added product appears in the Shopping Cart with the correct title and unit price.
- The cart item counter in the page header increments by 1.
- The cart subtotal equals the unit price of the added item.

### Actual Result
- To be filled during test execution.

### Status
- Pass / Fail

---

## Test Case ID: TC-CART-002B

### Title
Verify a guest user (not logged in) can add an item to the shopping cart without authenticating.

### Preconditions
- The user is **not** logged in (guest session).
- At least one product is available with stock > 0.

### Test Steps
1. Navigate to the platform homepage without logging in.
2. Search for a product and open its Product Detail Page (PDP).
3. Click the **Add to Cart** button.
4. Navigate to the Shopping Cart page.

### Expected Result
- The item is successfully added to the cart without any authentication prompt blocking the action.
- The Shopping Cart page displays the added item with its correct title and price.
- The cart item counter in the header increments by 1.

### Actual Result
- To be filled during test execution.

### Status
- Pass / Fail

---

## Test Case ID: TC-CART-002

### Title
Verify the shopping cart correctly recalculates the subtotal when an item's quantity is updated to a valid value.

### Preconditions
- The user is authenticated.
- The shopping cart contains exactly one item with a quantity of 1.
- The item has at least 2 units available in stock.

### Test Steps
1. Navigate to the Shopping Cart page.
2. Locate the item's quantity input field.
3. Change the quantity from `1` to `2`.
4. Click the **Update** button.

### Expected Result
- The item's line total accurately doubles (unit price x 2).
- The cart subtotal updates to reflect the new line total.
- No error or validation message is displayed.

### Actual Result
- To be filled during test execution.

### Status
- Pass / Fail

---

## Test Case ID: TC-CART-003

### Title
Verify a user can remove an item from the cart and the subtotal updates correctly.

### Preconditions
- The user is authenticated.
- The shopping cart contains at least 2 items.

### Test Steps
1. Navigate to the Shopping Cart page.
2. Note the current cart subtotal.
3. Click the **Remove** button (e.g., the red "X" or trash icon) next to one item.

### Expected Result
- The removed item disappears from the cart immediately.
- The cart subtotal decreases by the removed item's total value.
- The remaining items in the cart are unaffected (quantities and prices unchanged).
- The cart item counter in the header decreases by the removed item's quantity.

### Actual Result
- To be filled during test execution.

### Status
- Pass / Fail

---

## Test Case ID: TC-CART-005

### Title
Verify that adding a product already in the cart increments its quantity rather than creating a duplicate row.

### Preconditions
- The user is authenticated.
- The shopping cart contains exactly 1 unit of a specific item (e.g., "Item A").
- Item A has at least 2 units available in stock.

### Test Steps
1. Navigate to the Product Detail Page (PDP) of Item A.
2. Click the **Add to Cart** button a second time.
3. Navigate to the Shopping Cart page.

### Expected Result
- The cart does not create a new, separate row for Item A.
- The existing row for Item A shows its quantity incremented from 1 to 2.
- The line total for Item A doubles (unit price x 2).
- The cart subtotal reflects the updated total.

### Actual Result
- To be filled during test execution.

### Status
- Pass / Fail