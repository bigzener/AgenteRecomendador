package agentes.apriori;





import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteradorCombinacion implements Iterable<List<String>> {
	private List<String> lista;
	private Integer k;

	public IteradorCombinacion(List<String> s, Integer k) {
		lista = s;
		this.k = k;
	}

	@Override
	public Iterator<List<String>> iterator() {

		return new IteradorCombn(lista, k);
	}

	private class IteradorCombn implements Iterator<List<String>> {
		private int actualSize, maxresult;
		private Integer curIndex;
		private String[] result;
		private int[] indices;
		private String[] arrayList;
		private List<String> elem = null;

		public IteradorCombn(List<String> s, Integer k) {
			actualSize = k;// desde dónde
			curIndex = 0;
			maxresult = k;
			arrayList = new String[s.size()];
			for (int i = 0; i < arrayList.length; i++) { // la lista s la vuelca en arrayList
				arrayList[i] = s.get(i);
			}
			this.result = new String[actualSize < s.size() ? actualSize : s.size()]; 
			//el tamaño de result va a ser el valor menor entre actualSize y el tamaño de s
			indices = new int[result.length];

			for (int i = 0; i < result.length; i++) {
				indices[i] = result.length - 2 - i;
			}
		}

		public boolean hasNext() {
			elem = null;
			while ((elem == null && curIndex != -1)) {

				indices[curIndex]++;
				if (indices[curIndex] == (curIndex == 0 ? arrayList.length: indices[curIndex - 1])) {
					
					indices[curIndex] = indices.length - curIndex - 2;
					curIndex--;
				} else {

					result[curIndex] = arrayList[indices[curIndex]];
					
					if (curIndex < indices.length - 1)
						curIndex++;
					else {
						elem = new LinkedList<String>();
						for (String s : result) {
							elem.add(s);
						}

					}
				}
			}
			if (elem == null) {
				if (actualSize < maxresult) {
					actualSize++;
					this.result = new String[actualSize < arrayList.length ? actualSize
							: arrayList.length];
					indices = new int[result.length];

					for (int i = 0; i < result.length; i++) {

						indices[i] = result.length - 2 - i;
					}
					curIndex = 0;

					return this.hasNext();
				} else {
					return false;
				}
			} else {
				return true;
			}
		}

		@Override
		public List<String> next() {
			return elem;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}
	}
}
