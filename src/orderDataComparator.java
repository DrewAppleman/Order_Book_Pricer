import java.util.Comparator;


public class orderDataComparator  implements Comparator<orderData> {

	@Override
	public int compare(orderData o1, orderData o2) {
		if(o1.price>o2.price) return 1;
		else if(o1.price<o2.price) return -1;
		return 0;
	}
}


