package tutka.mateusz.windows;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractListBox;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;


public class FixedRadioBoxList extends AbstractListBox {
	
	private int checkedIndex;
	
	/**
	 * I had to copy whole logic form RadioBoxList to make {@code AbstractListBox#addItem(java.lang.Object)} public.
	 */
	public void addItem(Object item) {
       super.addItem(item);
    }
	

    /**
     * Creates a new RadioCheckBoxList with no items
     */
    public FixedRadioBoxList() {
        this(null);
    }

    /**
     * Creates a new RadioCheckBoxList with a specified size.
     * @param preferredSize Size of the RadioCheckBoxList or {@code null} to use the default
     * calculation algorithm
     */
    public FixedRadioBoxList(TerminalSize preferredSize) {
        super(preferredSize);
        this.checkedIndex = -1;
    }

    @Override
    protected ListItemRenderer createDefaultListItemRenderer() {
        return new RadioBoxListItemRenderer();
    }

    @Override
    public Result handleKeyStroke(KeyStroke keyStroke) {
        if(keyStroke.getKeyType() == KeyType.Enter ||
                (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == ' ')) {
            checkedIndex = getSelectedIndex();
            invalidate();
            return Result.HANDLED;
        }
        return super.handleKeyStroke(keyStroke);
    }

    @Override
    public void clearItems() {
        checkedIndex = -1;
        super.clearItems();
    }

    /**
     * This method will see if an object is the currently selected item in this RadioCheckBoxList
     * @param object Object to test if it's the selected one
     * @return {@code true} if the supplied object is what's currently selected in the list box,
     * {@code false} otherwise. Returns null if the supplied object is not an item in the list box.
     */
    public Boolean isChecked(Object object) {
        if(object == null)
            return null;

        if(indexOf(object) == -1)
            return null;

        return checkedIndex == indexOf(object);
    }

    /**
     * This method will see if an item, addressed by index, is the currently selected item in this
     * RadioCheckBoxList
     * @param index Index of the item to check if it's currently selected
     * @return {@code true} if the currently selected object is at the supplied index,
     * {@code false} otherwise. Returns false if the index is out of range.
     */
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean isChecked(int index) {
        if(index < 0 || index >= getItemCount()) {
            return false;
        }

        return checkedIndex == index;
    }

    /**
     * Sets the currently selected item by index. If the index is out of range, it does nothing.
     * @param index Index of the item to be selected
     */
    public void setCheckedItemIndex(int index) {
        if(index < -1 || index >= getItemCount())
            return;

        checkedIndex = index;
        invalidate();
    }

    /**
     * @return The index of the item which is currently selected, or -1 if there is no selection
     */
    public int getCheckedItemIndex() {
        return checkedIndex;
    }

    /**
     * @return The object currently selected, or null if there is no selection
     */
    public Object getCheckedItem() {
        if(checkedIndex == -1 || checkedIndex >= getItemCount())
            return null;

        return getItemAt(checkedIndex);
    }

    /**
     * Un-checks the currently checked item (if any) and leaves the radio check box in a state where no item is checked.
     */
    public void clearSelection() {
        checkedIndex = -1;
        invalidate();
    }

    public static class RadioBoxListItemRenderer extends ListItemRenderer {
        @Override
        protected int getHotSpotPositionOnLine(int selectedIndex) {
            return 1;
        }

        @Override
        protected String getLabel(AbstractListBox listBox, int index, Object item) {
            String check = " ";
            if(((FixedRadioBoxList)listBox).checkedIndex == index)
                check = "o";

            String text = (item != null ? item : "<null>").toString();
            return "<" + check + "> " + text;
        }
    }

	
	

}
