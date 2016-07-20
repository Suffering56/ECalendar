package gui.panel.ecalendar.frames.parents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;

import gui.panel.ecalendar.data.remote.RemoteService;

public abstract class ExtendFilterFrame<T> extends FilterFrame {

	public ExtendFilterFrame(RemoteService remote) {
		super(remote);
	}

	protected void init() {
		iterateElements(new Handler() {
			public void handle(JCheckBox box) {
				singleInit(box);
			}
		});
	}

	/**
	 * Перебирает все JCheckBox-компоненты, которые есть на форме, у которых
	 * есть атрибут "id" и, которых нет во множестве "noHandleComponents"
	 * 
	 * Подразумевается, что все эти компоненты - значения параметров фильтрации.
	 * 
	 * @param handler
	 *            - обработчик, который определяет, что делать с компонентами,
	 *            которые соответствуют указанным выше условиям
	 */
	protected void iterateElements(Handler handler) {
		filterCollection.clear();
		Iterator<?> iterator = swix.getIdComponentItertor();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof JCheckBox) {
				JCheckBox box = (JCheckBox) o;
				if (!noHandleComponents.contains(box)) {
					handler.handle(box);
				}
			}
		}
	}

	abstract protected void singleInit(JCheckBox box);

	protected void applyFilter() {
		iterateElements(
				new Handler() {
					public void handle(JCheckBox box) {
						if (stateMap.containsKey(box.getName())) {
							if (box.isSelected()) {
								addFilterCriteron(box);
								stateMap.put(box.getName(), true);
							} else {
								stateMap.put(box.getName(), false);
							}
						}
					}
				});

		sendRequest();
	}

	/**
	 * Задает какие JCheckBox - компоненты не нужно обрабатывать
	 * при итерации (iterateElements(Handler handler))
	 * 
	 * @param components
	 */
	protected void initNoHandleComponents(JCheckBox... components) {
		noHandleComponents.clear();
		noHandleComponents.addAll(Arrays.asList(components));
	}

	abstract protected void addFilterCriteron(JCheckBox box);

	abstract protected void sendRequest();

	protected JCheckBox enableCheckBox;
	protected Set<JCheckBox> noHandleComponents = new HashSet<JCheckBox>();

	protected Map<String, Boolean> stateMap;
	protected List<T> filterCollection = new ArrayList<T>();

	protected interface Handler {
		void handle(JCheckBox box);
	}
}
