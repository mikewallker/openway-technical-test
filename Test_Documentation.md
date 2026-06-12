# Part 1: Components of a Test Case

A standard test case should include:

- **Test Case ID:** A unique identifier (e.g., TC-CART-001).
- **Title:** A brief description of what is being tested.
- **Preconditions:** State of the application required before testing.
- **Test Steps:** Clear, actionable steps to execute the test.
- **Expected Result:** The exact behavior expected after executing the steps.
- **Actual Result:** (Filled during execution) What actually happened.
- **Status:** Pass/Fail.

Example:

# Test Case ID: TC-AUTH-001

## Title
Verify successful user login with valid credentials.

## Preconditions
- The user possesses a registered account with valid credentials.
- The user is navigated to the application's Login page.

## Test Steps
1. Enter the valid registered email address into the **Email** input field.
2. Enter the corresponding valid password into the **Password** input field.
3. Click the **Sign In** button.

## Expected Result
- The system successfully authenticates the credentials.
- The user is redirected to the authenticated Dashboard/Homepage.
- A **Welcome** confirmation is displayed in the UI.

## Actual Result
- The credentials were accepted after clicking **Sign In**.
- The user was **not redirected** to the Dashboard/Homepage and remained on the Login page.
- No **Welcome** confirmation message was displayed.
- A browser console error (`500 Internal Server Error`) was observed during the login request.

## Status
**Failed**

# Part 2.1: Test Case for Scenario B (Periplus Shopping Cart)

# Test Coverage Summary

This test suite covers the core shopping cart functionality, input validation, business constraints, inventory synchronization, and pricing calculations.

## Functional
- Add item to cart
- Remove item from cart
- Update quantity
- Cart persistence after login/logout
- Checkout flow validation

## Validation
- Non-numeric quantity input
- Zero quantity
- Negative quantity
- Quantity exceeding available stock

## Business Rules
- Maximum 10 shops per cart (reference taken from Tokopedia, one of the biggest E-commerce platform in Indonesia)
- Maximum 400 total items per cart (reference taken from Tokopedia)
- Duplicate item handling

## Inventory Synchronization
- Product removed from catalog
- Product becomes out of stock
- Product restocked

## Pricing
- Discounted item subtotal calculation
- Cart subtotal calculation

## Test Case ID: TC-CART-001

**Title:** Verify a user can add a book to the shopping cart.

**Preconditions:**
- The user is on the Periplus homepage.
- The user is logged in.
- The user's shopping cart is completely empty.
- There is at least one product available on the website with stock > 0.

**Test Steps:**
1. Search for a book.
2. Click on the desired book from the search results.
3. Click the **Add to Cart** button.
4. Navigate to the Shopping Cart page.

**Expected Result:**
- The shopping cart page displays the added book.
- The cart subtotal increases by the book price.

**Actual Result:**
- The application behaved as expected.

**Status:**
- Passed

## Test Case ID: TC-CART-002 

**Title:** Verify the shopping cart updates the total price when the item quantity is increased.

**Preconditions:**
- The user is logged in.
- The user has at least one item in their shopping cart.

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Change the quantity of the item from `1` to `2` in the quantity input box.
3. Click the **Update** (Refresh) button next to the quantity.

**Expected Result:**
- The item's total price accurately doubles.
- The subtotal for the entire cart updates to reflect the new amount.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

---

## Test Case ID: TC-CART-003

**Title:** Verify a user can remove an item from the shopping cart.

**Preconditions:**
- The user is logged in.
- The user has at least one item in their shopping cart.

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Click the **Remove** (red 'X' or trash can) button next to the item.

**Expected Result:**
- The item is removed from the cart page.
- The total price decreases accordingly.
- The cart counter at the top of the page decreases by 1.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

---

## Test Case ID: TC-CART-004

**Title:** Verify cart persistence after the user logs out and logs back in.

**Preconditions:**
- The user is logged in.
- The user has at least one item in their shopping cart.

**Test Steps:**
1. Click the **Logout** button.
2. Navigate back to the login page and sign in with the same credentials.
3. Navigate to the Shopping Cart page.

**Expected Result:**
- The item added before logging out is still present in the shopping cart.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

## Test Case ID: TC-CART-005 

**Title:** Verify shopping cart behavior when adding a duplicate item that already exists in the cart.

**Preconditions:**
- The user is logged in.
- The shopping cart already contains exactly 1 unit of a specific book (e.g., "Harry Potter").

**Test Steps:**
1. Search for the identical book ("Harry Potter") again.
2. Click on the book to open its product details page.
3. Click the **Add to Cart** button a second time.
4. Navigate to the Shopping Cart page.

**Expected Result:**
- The cart does not create a separate duplicate line item or new row for the book.
- The existing row's quantity automatically increments from 1 to 2.
- The unit price remains the same.
- The total price for that line item updates correctly.
- The entire cart subtotal updates accurately to reflect the second item.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

---

## Test Case ID: TC-CART-006 

**Title:** Verify shopping cart can hold and display multiple distinct items simultaneously.

**Preconditions:**
- The user is logged in.
- The shopping cart already contains exactly 1 unit of a specific book (e.g., "Book A").

**Test Steps:**
1. Search for a completely different book (e.g., "Book B").
2. Click on "Book B" from the search results to open its details page.
3. Click the **Add to Cart** button.
4. Navigate to the Shopping Cart page.

**Expected Result:**
- The shopping cart page displays both "Book A" and "Book B" as two separate rows.
- The item counters and quantities for both items are preserved accurately.
- The final cart subtotal displays the exact mathematical sum of both distinct items combined.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

## Test Case ID: TC-CART-007

**Title:** Verify the shopping cart prevents users from exceeding the available stock quantity.

**Preconditions:**
- The user is logged in.
- The shopping cart contains at least one item.
- The item has a known stock quantity available (e.g., 10 units remaining).

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Locate the item with available stock of 10 units.
3. Enter a quantity greater than the available stock (e.g., 999 or 11).
4. Click the **Update** (Refresh) button.

**Expected Result:**
- The system does not allow the quantity to exceed the available stock.
- An appropriate validation message is displayed (e.g., "Only 10 units available in stock").
- The quantity remains unchanged or is automatically adjusted to the maximum available stock.
- The cart subtotal is calculated using a valid quantity only.
- No system errors or unexpected behavior occur.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

## Test Case ID: TC-CART-008

**Title:** Verify the shopping cart prevents users from entering zero or negative quantities.

**Preconditions:**
- The user is logged in.
- The shopping cart contains at least one item.

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Enter `0` into the quantity input field for an item.
3. Click the **Update** (Refresh) button.
4. Repeat the test by entering a negative value (e.g., `-1`).
5. Click the **Update** button again.

**Expected Result:**
- The system rejects quantities less than 1.
- An appropriate validation message is displayed (e.g., "Quantity must be greater than 0").
- The item quantity remains unchanged or is automatically reset to the minimum valid value (1).
- The cart subtotal is not updated using the invalid quantity.
- No system errors or unexpected behavior occur.

**Actual Result:**
- To be filled during test execution.

**Status:**
- Pass / Fail

## Test Case ID: TC-CART-009

**Title:** Verify the shopping cart only accepts numeric values for item quantity.

**Preconditions:**
- The user is logged in.
- The shopping cart contains at least one item.

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Enter a non-numeric value (e.g., "abc", "@#$", or "1a") into the quantity field.
3. Click the **Update** button.

**Expected Result:**
- The system rejects the input.
- An appropriate validation message is displayed.
- The quantity remains unchanged.
- No system errors occur.

## Test Case ID: TC-CART-010

**Title:** Verify checkout is blocked when the shopping cart is empty.

**Preconditions:**
- The user is logged in.
- The shopping cart contains no items.

**Test Steps:**
1. Navigate to the Shopping Cart page.
2. Click the **Checkout** button.

**Expected Result:**
- Checkout does not proceed.
- An appropriate warning message is displayed indicating the cart is empty.

## Test Case ID: TC-CART-011

**Title:** Verify a user cannot add items from an eleventh shop when the cart already contains items from 10 different shops.

**Preconditions:**
- The user is logged in.
- The cart contains items from 10 different shops.

**Test Steps:**
1. Navigate to an item belonging to an eleventh shop.
2. Click **Add to Cart**.

**Expected Result:**
- The item is not added.
- An appropriate validation message is displayed indicating the maximum number of shops has been reached.

## Test Case ID: TC-CART-012

**Title:** Verify adding another item from an existing shop succeeds even when the cart contains many items to validate restriction is based on distinct shops, not item count.

**Preconditions:**
- The user is logged in.
- The cart contains 20 items from the same shop.

**Test Steps:**
1. Navigate to another item from the same shop.
2. Click **Add to Cart**.

**Expected Result:**
- The item is successfully added.
- No shop-limit validation is triggered.

## Test Case ID: TC-CART-013

**Title:** Verify a user cannot exceed the maximum cart quantity limit using a single item.

**Preconditions:**
- The user is logged in.
- The cart contains one item with quantity 400.

**Test Steps:**
1. Increase the item's quantity to 401.

**Expected Result:**
- The action is rejected.
- An appropriate validation message is displayed.
- Total cart quantity remains 400.

## Test Case ID: TC-CART-014

**Title:** Verify a user cannot exceed the maximum cart quantity limit using multiple items.

**Preconditions:**
- The user is logged in.
- The cart contains:
    - Item A: quantity 200
    - Item B: quantity 200
- Both items have sufficient stock.

**Test Steps:**
1. Attempt to increase either item's quantity by 1.

**Expected Result:**
- The action is rejected.
- An appropriate validation message is displayed.
- Total cart quantity remains 400.

## Test Case ID: TC-CART-015

**Title:** Verify a user cannot add a new item when the total cart quantity limit is already reached.

**Preconditions:**
* The user is logged in.
* The cart currently contains an item with a quantity of 400 (representing the maximum allowed items in the cart).

**Test Steps:**
1. Navigate to the product page of a different available item.
2. Click **Add to Cart** to attempt adding this new item.

**Expected Result:**
* The action fails, and the new item is not added to the cart.
* A warning message is displayed indicating that the maximum cart capacity has been reached.
* The total item quantity in the cart remains at 400.

## Test Case ID: TC-CART-016

**Title:** Verify removed products are automatically removed from the shopping cart.

**Preconditions:**
- The user is logged in.
- The cart contains an item.

**Test Steps:**
1. Delete the item from the seller's catalog.
2. Refresh or reopen the shopping cart.

**Expected Result:**
- The deleted item is removed from the cart.
- The cart subtotal is updated accordingly.

## Test Case ID: TC-CART-017

**Title:** Verify out-of-stock items are moved to a non-checkout section.

**Preconditions:**
- The user is logged in.
- The cart contains an item.

**Test Steps:**
1. Set the item's stock to 0.
2. Refresh or reopen the shopping cart.

**Expected Result:**
- The item is moved to a dedicated out-of-stock section.
- The item is excluded from checkout calculations.
- The item cannot be selected for checkout.

## Test Case ID: TC-CART-018

**Title:** Verify restocked items are moved back into the active cart section.

**Preconditions:**
- The user is logged in.
- The cart contains an item in the out-of-stock section.

**Test Steps:**
1. Replenish the item's stock.
2. Refresh or reopen the shopping cart.

**Expected Result:**
- The item reappears in the active cart section.
- The item becomes available for checkout again.

## Test Case ID: TC-CART-019

**Title:** Verify discounted items contribute the correct discounted amount to the cart subtotal.

**Preconditions:**
- The user is logged in.
- A product has an active discount.

**Test Steps:**
1. Add the discounted product to the cart.
2. Navigate to the Shopping Cart page.

**Expected Result:**
- The item's displayed price matches the discounted price.
- The cart subtotal increases by the discounted price, not the original price.
- All totals are calculated correctly.