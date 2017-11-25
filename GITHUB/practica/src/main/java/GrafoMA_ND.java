
/**
 * Esta clase representa a un Grafo NO Dirigido con aristas ponderadas (con valores double) implementado con una
 * matriz de adyacencia. Este tipo de Grafo se crea con un conjunto de vertices concreto (identificados del 0..V-1)
 * no dinamico (una vez creado el objeto pueden incluirse y eliminarse aristas, pero el numero de vertices es estatico)
 */

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class GrafoMA_ND extends Grafo {

	private static final double YA_VISITADO = Double.MAX_VALUE;
	protected double[][] grafo;

	public GrafoMA_ND(int numVertices) {
		super(numVertices, 0);
		grafo = new double[numVertices][numVertices];
		for (int v = 0; v < numVertices; v++)
			for (int vAd = 0; vAd < numVertices; vAd++)
				grafo[v][vAd] = SIN_ARISTA;
	}

	/**
	 * Comprueba si una arista concreta se encuentra en el grafo
	 * 
	 * @param arista
	 *            Arista a buscar en el grafo (vertices origen y destino)
	 * @return true si en el grafo existe la arista que une los vertice origen y
	 *         destino. False en caso contrario
	 */
	public boolean existeArista(Arista arista) {
		return grafo[arista.getOrigen()][arista.getDestino()] != SIN_ARISTA;
	}

	/**
	 * Devuelve el peso de la arista
	 * 
	 * @param arista
	 *            Arista cuyo peso se desea buscar en el grafo (vertices origen
	 *            y destino)
	 * @return peso de la arista, en caso de que exista, o
	 *         Double.POSITIVE_INFINITY en caso de que la arista no exista
	 */
	public double getPesoArista(Arista arista) {
		return grafo[arista.getOrigen()][arista.getDestino()];
	}

	/**
	 * Inserta la arista (vertices origen y destino) en el grafo (si no existe).
	 * En caso de que la arista ya exista este metodo no tiene ningun efecto en
	 * el grafo. En caso de insertarse la arista, el peso asignado es 1.
	 * 
	 * @param arista
	 *            Arista a insertar en el grafo (vertices origen y destino)
	 */
	public void insertaArista(Arista arista) {
		insertaArista(arista, 1);
	}

	/**
	 * Inserta la arista (vertices origen y destino) en el grafo (si no existe)
	 * con el peso indicado. Al tratarse de un grafo no dirigido, se añaden las
	 * aristas (origen, destino) y (destino, origen), aunque sólo cuentan como
	 * 1 arista. En caso de que la arista ya exista este metodo no tiene ningun
	 * efecto en el grafo.
	 * 
	 * @param arista
	 *            Arista a insertar en el grafo (vertices origen y destino)
	 * @param peso
	 *            peso a asignar a la arista
	 */

	public void insertaArista(Arista arista, double peso) {
		if (!existeArista(arista)) {
			grafo[arista.getOrigen()][arista.getDestino()] = peso;
			grafo[arista.getDestino()][arista.getOrigen()] = peso;
			incNumAristas();

		}
	}

	/**
	 * Devuelve el vector que indica qué vertices son adyacentes al vertice
	 * origen
	 * 
	 * @param origen
	 *            vertice origen
	 * @return vetor de tamaño V qué indica qué vertices son adyacentes a
	 *         origen y con qué peso y cuales no (Double.POSITIVE_INFINITY)
	 */
	public double[] getAdyacentesDe(int origen) {
		return grafo[origen];
	}

	/**
	 * ESTE METODO DEBERA IMPLEMENTARSE OBLIGATORIAMENTE PARA QUE LA PRACTICA
	 * SEA ACEPTADA Este metodo debera devolver el camino mas corto entre los
	 * vertices origen y destino, asi como la distancia de este camino
	 * 
	 * @param origen
	 *            vertice origen (valor entre 0..numVertices-1)
	 * @param destino
	 *            vertice destino (valor entre 0..numVertices-1)
	 * @param distancia
	 *            objeto de tipo Real donde se debera devolver la longitud del
	 *            camino mas corto obtenido
	 * @return ArrayList de identificadores de los vertices del grafo que
	 *         recorre el camino mas corto obtenido. Este ArrayList debera
	 *         incluir como primer elemento el identificador origen y, como
	 *         ultimo elemento el identificador destino
	 */
	public ArrayList<Integer> caminoMasCorto(int origen, int destino, Real distancia) {
		ArrayList<Integer> camino = new ArrayList<Integer>();
		double[] menorCoste = new double[numVertices];
		int[] VerticeMasCercano = new int[numVertices];
		Dijkstra(origen,menorCoste,VerticeMasCercano);
		int i=destino;
			while(VerticeMasCercano[i]!=-1){
				camino.add(0, i);;
				i=VerticeMasCercano[i];
			}
			camino.add(0,origen);
		distancia.setValor(menorCoste[destino]);
		return camino;
	}

	public void Dijkstra(int origen, double[] menorCoste, int[] verticeMasCercano) {
		boolean[] visitados = new boolean[numVertices];
		int vertice;
		inicializarDijkstra(menorCoste, visitados, verticeMasCercano, origen);
		for (int i = 1; i < numVertices; i++) {
			vertice = seleccionarVertice(menorCoste, visitados);
			visitados[vertice] = true;
			for (int j = 0; j < numVertices; j++)
				if (!visitados[j])
					if (menorCoste[j] > (menorCoste[vertice] + grafo[vertice][j])) {
						menorCoste[j] = menorCoste[vertice] + grafo[vertice][j];
						verticeMasCercano[j] = vertice;
					}
		}
	}

	private void inicializarDijkstra(double[] menorCoste, boolean[] visitados, int[] VerticeMasCercano, int origen) {
		for (int i = 0; i < numVertices; i++) {
			visitados[i] = false;
			menorCoste[i] = grafo[origen][i];
			if (grafo[origen][i] != SIN_ARISTA)
				VerticeMasCercano[i] = origen;
			else
				VerticeMasCercano[i] = -1;
		}
		visitados[origen] = true;
	}

	private int seleccionarVertice(double[] menorCoste, boolean[] visitados) {
		int vertice = 0;
		double menor;
		menor = Integer.MAX_VALUE;
		for (int i = 0; i < numVertices; i++)
			if (!visitados[i] && (menorCoste[i] < menor)) {
				menor = menorCoste[i];
				vertice = i;
			}
		return vertice;
	}

	/**
	 * ESTE METODO DEBERA IMPLEMENTARSE OBLIGATORIAMENTE PARA QUE LA PRACTICA
	 * SEA ACEPTADA Obtiene los puentes del grafo (aristas cuya eliminacion
	 * haria que el grafo quedase fragmentado en mas de una componente conexa)
	 * 
	 * @return ArrayList de aristas identificadas como puentes
	 */
	public ArrayList<Arista> getPuentes() {
		ArrayList<Arista> puentes = new ArrayList<Arista>();
		for (int i = 0; i < getNumVertices(); i++) {
			for (int j = i; j < getNumVertices(); j++) {
				if (grafo[i][j] != SIN_ARISTA) {
					double aux = grafo[i][j];
					grafo[i][j] = SIN_ARISTA;
					grafo[j][i] = SIN_ARISTA;
					numAristas--;
					if (!conexo()) {
						puentes.add(new Arista(i, j));
					}
					insertaArista(new Arista(i, j), aux);
				}
			}
		}
		return puentes;
	}
	
	  private boolean conexo() { 
	  	boolean[] visitados = new boolean[numVertices]; 
	  	for (int i = 0; i < numVertices; i++) 
	  		visitados[i]= false; 
	  	recorrerConexo(visitados); 
	  	return todosVisitados(visitados); 
	  }

	private void recorrerConexo(boolean[] visitados) {
		visitar_DFS_Componente(0, visitados);
	}
	
	/* METODO DUPLICADO
	  private void visitar_DFS_Componente(int origen, boolean[] visitados) {
		visitados[origen] = true;
		for (int ady = 0; ady < numVertices; ady++)
			if (grafo[origen][ady] != SIN_ARISTA)
				if (!visitados[ady])
					visitar_DFS_Componente(ady, visitados);
	}
	*/
	
	  private boolean todosVisitados(boolean[] visitados) {
	  boolean ok = true;
	  int i = 0;
	  while ((i < visitados.length) && ok) {
	  	ok = visitados[i];
	 	i++;
	  }
	  return ok;
	  }

	/**
	 * LA IMPLEMENTACION DE ESTE METODO ES OPCIONAL PARA PODER ENTREGAR LA
	 * PRACTICA (2 PUNTOS) Obtiene las articulaciones del grafo (vertices cuya
	 * eliminacion haria que el grafo quedase fragmentado en mas de una
	 * componente conexa)
	 * 
	 * @return ArrayList de identificadores de los vertices del grafo de tipo
	 *         articulacion
	 */
	  
	  //Implementacion 1
	  /*
	   * Esta primera implementacion consiste en coger cada vertice y eliminar todas sus aristas 
	   * (marcandolas como SIN_ARISTA) y despues con el metodo componentes comprobar si el numero de
	   * componentes es mayor que 2, esto se debe a que cuando desconectamos un nodo entero para ver 
	   * si es critico y efectivamente lo es, obtenemos 3 componentes conexas (en este caso particular)
	   * siendo el propio nodo una componente conexa independiente, y por eso se establece la condicion 
	   * en componentes()>2 y no componentes()==2.
	   * El orden de complejidad de esta solucion es O(N^3)
	   */
	public ArrayList<Integer> getArticulaciones() {
		ArrayList<Integer> articulaciones = new ArrayList<Integer>();
		for (int i = 0; i < numVertices; i++) {
			double[] fila = new double[numVertices];
			double[] columna = new double[numVertices];
			for (int j = 0; j < numVertices; j++) {
				fila[j] = grafo[i][j];
				grafo[i][j] = SIN_ARISTA;
				columna[j] = grafo[j][i];
				grafo[j][i] = SIN_ARISTA;
				numAristas--;
			}
			if (componentes() > 2) {
				articulaciones.add(i);
			}
			for (int j = 0; j < numVertices; j++) {
				grafo[i][j] = fila[j];
				grafo[j][i] = columna[j];
				numAristas++;
			}
		}
		return articulaciones;
	}

	private int componentes() {
		int numComp = 0;
		boolean[] visitados = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++)
			visitados[i] = false;
		for (int i = 0; i < numVertices; i++) {
			if (!visitados[i]) {
				recorrerConexo(i, visitados);
				numComp++;
			}
		}
		return numComp;
	}


	private void recorrerConexo(int pos, boolean[] visitados) { // Metodo Sobrecargado
		visitar_DFS_Componente(pos, visitados);
	}

	private void visitar_DFS_Componente(int origen, boolean[] visitados) {
		visitados[origen] = true;
		for (int ady = 0; ady < numVertices; ady++)
			if (grafo[origen][ady] != SIN_ARISTA)
				if (!visitados[ady])
					visitar_DFS_Componente(ady, visitados);
	}
	
	//Implementacion 2 (Alternativa)
	/* 
	 * En este caso la implementacion consiste en ir cogiendo uno por uno los vertices del grafo 
	 * y llegado el momento de expandirlo, no hacerlo, lo que provoca que no se tengan en cuenta 
	 * sus aristas en lugar de tener que eliminarlas.
	 * Esta implementacion reduce el orden de complejidad, pues pasa de ser O(N^3) a ser O(N^2).
	 * Al ser la solucion con menor complejidad, es la solucion que se aplica en el metodo
	 * getAlmacenesCriticos() de RedLogistica.
	 */
	
	public ArrayList<Integer> getArticulaciones2() {
		ArrayList<Integer> articulaciones = new ArrayList<Integer>();
		for(int i=0; i<numVertices;i++){
			if(!conexo2(i)){
				articulaciones.add(i);
			}
		}
		return articulaciones;
	}
	
	private boolean conexo2(int critico){
		boolean[] visitados = new boolean[numVertices]; 
	  	for (int j = 0; j < numVertices; j++) 
	  		visitados[j]= false; 
	  	recorrerConexo2(critico,visitados); 
	  	return todosVisitados2(visitados,critico);  
	}
	
	private void recorrerConexo2(int critico, boolean[] visitados) {
		visitar_DFS_Componente2(0, visitados,critico);
	}
	
	private void visitar_DFS_Componente2(int origen, boolean[] visitados,int critico) {
		visitados[origen]=true;
		for (int ady = 0; ady < numVertices; ady++)
			if (grafo[origen][ady] != SIN_ARISTA && ady != critico)
				if (!visitados[ady])
					visitar_DFS_Componente2(ady, visitados,critico);
	}
	
	private boolean todosVisitados2(boolean[] visitados, int critico) {
		  boolean ok = true;
		  int i = 0;
		  while ((i < visitados.length) && ok) {
		  	if(i != critico){
			  ok = visitados[i];
		  	}
		  	i++;
		  }
		  return ok;
		  }
	
	/**
	 * LA IMPLEMENTACION DE ESTE METODO ES OPCIONAL PARA PODER ENTREGAR LA
	 * PRACTICA (2 PUNTOS) Obtiene los centros del grafo (vertices que tienen la
	 * menor de las excentricidades del grafo, siendo la excentricidad de un
	 * vertice la maxima de las distancias entre cualquiera de los vertices del
	 * grafo y el vertice en cuestion)
	 * 
	 * @return ArrayList de identificadores de los vertices del grafo de tipo
	 *         centro
	 */
	public ArrayList<Integer> getCentros() {
		ArrayList<Integer> centros = new ArrayList<Integer>();
		double[][] caminos=new double[numVertices][numVertices];
		int[][]vertices=new int[numVertices][numVertices];
		double[]excentricidad=new double [numVertices];
		Floyd(caminos,vertices );
		
		for(int i=0;i<numVertices;i++){
			excentricidad[i]=excentricidadM(caminos,i);
		}
		double aux=menor(excentricidad);
		for(int a=0;a<numVertices;a++){
			for(int b=a;b<numVertices;b++)
			if(caminos[b][a]==aux)centros.add(a);
		}
		return centros;
	}
	private double excentricidadM(double[][] caminos,int origen){
		
		double mayor=0;
		for(int i=0;i<numVertices;i++){
			if(caminos[i][origen]>mayor)mayor=caminos[i][origen];
		}
		return mayor;
	}
	private double menor(double[] a){
		double aux=YA_VISITADO;
		for(int i=a.length-1;i>=0;i--){
			if(a[i]<aux&& a[i]!=0)aux=a[i];		
		}
		return aux;
	}
	private void inicializarFloyd(double[][] caminosMinimos, int[][] verticeMasCercano) {
		final int SIN_PREVIO = -1;
		for (int i = 0; i < numVertices; i++)
			for (int j = 0; j < numVertices; j++) {
				caminosMinimos[i][j] = grafo[i][j];
				if (grafo[i][j] != SIN_ARISTA)
					verticeMasCercano[i][j] = i;
				else
					verticeMasCercano[i][j] = SIN_PREVIO;
			}
		for (int i = 0; i < numVertices; i++) {
			caminosMinimos[i][i] = 0;
			verticeMasCercano[i][i] = SIN_PREVIO;
		}
	}

	public void Floyd(double[][] caminosMinimos, int[][] verticeMasCercano) {
		inicializarFloyd(caminosMinimos, verticeMasCercano);
		for (int k = 0; k < numVertices; k++)
			for (int i = 0; i < numVertices; i++)
				if ((i != k) && (caminosMinimos[i][k] != SIN_ARISTA))
					for (int j = 0; j < numVertices; j++)
						if ((k != j) && (caminosMinimos[k][j] != SIN_ARISTA)
								&& (caminosMinimos[i][k] + caminosMinimos[k][j] < caminosMinimos[i][j])) {
							caminosMinimos[i][j] = caminosMinimos[i][k] + caminosMinimos[k][j];
							verticeMasCercano[i][j] = k;
						}
	}

}