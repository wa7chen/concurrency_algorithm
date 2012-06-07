package search.binarySearch;

public class BinarySearch {
	public static int binSearch(int[] counts,int aim){
		return binSearch(counts ,aim,0,counts.length);
	}
	public static int binSearch(int[] counts,int aim,int left,int right){
		if(left <= right){
			int mid = (left + right) / 2;
			if(aim == counts[mid]){
				return mid ;
			}
			else if(aim > counts[mid]){
				 return binSearch(counts,aim,mid + 1,right);
			}
			else{
				return binSearch(counts,aim,left,mid - 1 );
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		int[] counts = {1,3,6,8,17};
		System.out.println("ÕÒµ½8ÔÚ" + binSearch(counts,8));
	}
}
