package br.gov.jfrj.siga.hibernate;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.hibernate.ext.MontadorQuery;

/**
 * Classe que trata da lógica de carregamento da extensão de busca textual em outro classloader. 
 * Isso é necessário, pois durante o redeploy da aplicação é necessário um reinício das instâncias do servidor de aplicação.
 * @author kpf
 *
 */
public class CarregadorPlugin {

	private static final Logger log = Logger.getLogger(CarregadorPlugin.class);

	private static final String DEFAULT_FILESYSTEM_SEPARATOR = FileSystems.getDefault().getSeparator();
	private static final String DEFAULT_PROTOCOL = "file://";

	private ClassLoaderRecarregavel classloader;

	/**
	 * Inicializa o carregador de plugins usando o mesmo classpath do classloader em execução
	 */
	public CarregadorPlugin() {
		try {
			final Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("/br");
			final List<URL> list = Collections.list(resources);
			final ListIterator<URL> iterator = list.listIterator();
			while(iterator.hasNext()){
				URL url = iterator.next();
				if (!url.getPath().contains("siga-ext")) {
					iterator.remove();
				} else {
					final String path = url.getPath().replaceAll("\\.jar.*", ".jar");
					final String absolutePath = path.startsWith(DEFAULT_FILESYSTEM_SEPARATOR) ? DEFAULT_PROTOCOL + path : path;
					iterator.set(new URL(absolutePath));
				}
			}
			URL implDefault = Thread.currentThread().getContextClassLoader().getResource("br/gov/jfrj/siga/hibernate/ext/MontadorQuery.class");
			list.add(new URL(implDefault.toExternalForm().toString().replaceAll("\\.jar.*", ".jar").replace("vfs:", "file:")));
			
			URL[] classpath = new URL[list.size()];
			list.toArray(classpath);
			
			this.classloader = new ClassLoaderRecarregavel(classpath);
		} catch (IOException e) {
			log.error("Não foi possível criar o classloader recarregável.", e);
		}
	}

	/**
	 * Retorna o componente padrão. Normalmente utilizada quando um plugin não está disponível
	 * @return - instância padrão
	 */
	public IMontadorQuery getMontadorQueryDefault() {
		try {
			return new MontadorQuery();
		} catch (Exception e) {
			log.error("Não foi possível instanciar o MontadorQuery default. "
					+ "Será utilizado o MontadorQuery no mesmo classloader da "
					+ "aplicação e isso poderá exigir o reinicio da instância do "
					+ "servidor de aplicação durante o redeploy", e);
		}
		return null;
	}

	/**
	 * Retona a implementação disponibilizada pelo plugin
	 * @return - instância personalizada pelo plugin
	 */
	public IMontadorQuery getMontadorQueryImpl() {
		try {
			return (IMontadorQuery) Class.forName(Prop.get("montador.query"), true, this.classloader).newInstance();
		} catch (Exception e) {
			log.debug("Não foi possível instanciar o MontadorQuery do plugin: prosseguindo com o carregamento da instância padrão.", e);
		}
		return getMontadorQueryDefault();
	}

}
