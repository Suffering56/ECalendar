package gui.panel.ecalendar.data.remote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gui.panel.ecalendar.data.ExtendCalendarRow;
import gui.panel.ecalendar.frames.FrameDetails;
import gui.panel.ecalendar.frames.parents.DataFrame;
import gui.panel.ecalendar.frames.parents.DataFrameInterface;
import p.calendar.InfoCalendarAPI;
import p.calendar.data.CalendarRow;
import p.calendar.interf.ICalendarChangeEvent;
import p.calendar.interf.IFreeUpdatesListener;
import prime_tass.connect.client_api.ConnectionClientAPI;

public abstract class DefaultRemoteService implements RemoteService {

	public DefaultRemoteService(DataFrame parent, boolean update) {
		this.parent = parent;
		this.update = update;
		this.connectionClient = ServerConnector.getInstance().getConnectionClient();

		initCalendarAPI();
		initResponseListener();
	}

	private void initCalendarAPI() {
		updatesListener = new IFreeUpdatesListener() {
			public void arrivedFreeUpdates(CalendarRow[] rows) {
				if (update) {
					for (CalendarRow row : rows) {
						Long id = row.getid();
						if (idSet.contains(id)) {
							for (ExtendCalendarRow extRow : modelRowsList) {
								if (id == extRow.getId()) {
									extRow.update(row);
								}
							}
						}
					}
					parent.updateModel(modelRowsList);
				}
			}

			public void internalErrorOurured(String error) {
				System.out.println("Error ouccured: " + error);
			}
		};

		api = new InfoCalendarAPI(updatesListener);
		connectionClient.assignSink(api.getNetworkInterface());
		//App.getInstance().linkToConnection(api.getNetworkInterface(), TConstants.INFO_SERVER_MANAGER);
	}

	private void initResponseListener() {
		responseListener = new ICalendarChangeEvent() {
			public void calendarEvent(CalendarRow[] rows) {

				if (!(parent instanceof FrameDetails)) {
					/**
					 * Для главной таблицы - всегда обновляем модель
					 */
					updateParentData(rows, true);
				} else {
					/**
					 * Для таблицы "Details" проверяем, если ли данные.
					 * Если данных нет - то делаем кнопку "Показать еще" неактивной
					 */
					boolean update = rows.length > 0;
					updateParentData(rows, update);
				}
			}

			public void internalErrorOurured(String error) {
				parent.setDefaultCursor();
				System.out.println("Error ouccured: " + error);
			}
		};
	}

	private void updateParentData(CalendarRow[] rows, boolean update) {
		/**
		 * Обновляем модель.
		 */
		if (update) {
			try {
				idSet.clear();
				modelRowsList = new ArrayList<ExtendCalendarRow>();
				for (CalendarRow row : rows) {
					idSet.add(row.getid());
					modelRowsList.add(new ExtendCalendarRow(row));
				}
				parent.updateModel(modelRowsList);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				parent.setDefaultCursor();
			}
		} else {
			/**
			 * НЕ обновляем модель
			 */
			if (modelRowsList == null) {
				modelRowsList = new ArrayList<ExtendCalendarRow>();
			}
			parent.updateModel(modelRowsList);
			FrameDetails frameDetails = (FrameDetails) parent;
			frameDetails.showMoreBtn.setEnabled(false);
			parent.setDefaultCursor();
		}
	}

	protected void mainRequest(Calendar startDate, Calendar endDate, String filter, InfoCalendarAPI.COLUMN sortColumn,
			InfoCalendarAPI.DIRECTION sortDirection) {

		saveRequestState(startDate, endDate, filter, sortColumn, sortDirection);
		api.requestCalendarEvents(startDate.getTime(), endDate.getTime(), filter, sortColumn, sortDirection, responseListener);
		parent.setWaitCursor();
	}

	public void removeSink() {
		connectionClient.removeSink(api.getNetworkInterface());
		//App.getInstance().unlinkFromConnection(api.getNetworkInterface(), TConstants.INFO_SERVER_MANAGER);
	}

	abstract protected void saveRequestState(Calendar startDate, Calendar endDate, String filter, InfoCalendarAPI.COLUMN sortColumn,
			InfoCalendarAPI.DIRECTION sortDirection);

	private DataFrameInterface parent;
	private boolean update;
	private ConnectionClientAPI connectionClient = null;
	private InfoCalendarAPI api;
	private IFreeUpdatesListener updatesListener;
	private ICalendarChangeEvent responseListener;

	private Set<Long> idSet = new HashSet<Long>();
	private List<ExtendCalendarRow> modelRowsList;
}
