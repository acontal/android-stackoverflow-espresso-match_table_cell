package com.example.test;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import static com.google.common.base.Preconditions.*;
import static org.hamcrest.Matchers.*;

import org.hamcrest.*;

import android.test.ActivityInstrumentationTestCase2;
import android.view.*;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	public void testPreconditions() {
		getActivity();
	}
	
	public void testCellBelowFoodGroup() {
		getActivity();
		onView(
			allOf(
				isDescendantOfA(isAssignableFrom(TableLayout.class)),
				isInRowBelow(withText("Food Group")),
				hasChildPosition(0)
				)
		).check(matches(withText("TEXT TO BE FOUND")));
	}

	static Matcher<View> isInRowBelow(final Matcher<View> viewInRowAbove) {
		checkNotNull(viewInRowAbove);
		return new TypeSafeMatcher<View>(){

			@Override
			public void describeTo(Description description) {
				description.appendText("is below a: ");
				viewInRowAbove.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				// Find the current row
				ViewParent viewParent = view.getParent();
				if (!(viewParent instanceof TableRow)) {
					return false;
				}
				TableRow currentRow = (TableRow) viewParent;
				// Find the row above
				TableLayout table = (TableLayout) currentRow.getParent();
				int currentRowIndex = table.indexOfChild(currentRow);
				if (currentRowIndex < 1) {
					return false;
				}
				TableRow rowAbove = (TableRow) table.getChildAt(currentRowIndex - 1);
				// Does the row above contains at least one view that matches viewInRowAbove?
				for(int i = 0 ; i < rowAbove.getChildCount() ; i++) {
					if (viewInRowAbove.matches(rowAbove.getChildAt(i))) {
						return true;
					}
				}
				return false;
			}};
	}

	static Matcher<View> hasChildPosition(final int i) {
		return new TypeSafeMatcher<View>(){

			@Override
			public void describeTo(Description description) {
				description.appendText("is child #" + i);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent viewParent = view.getParent();
				if (!(viewParent instanceof ViewGroup)) {
					return false;
				}
				ViewGroup viewGroup = (ViewGroup) viewParent;
				return (viewGroup.indexOfChild(view) == i);
			}};
	}
	
}
