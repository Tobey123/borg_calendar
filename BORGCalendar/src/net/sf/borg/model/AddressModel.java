/*
This file is part of BORG.
 
    BORG is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    BORG is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with BORG; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
Copyright 2003 by Mike Berger
 */
package net.sf.borg.model;

import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.borg.common.Errmsg;
import net.sf.borg.common.PrefName;
import net.sf.borg.common.Prefs;
import net.sf.borg.common.XTree;
import net.sf.borg.model.beans.Address;
import net.sf.borg.model.beans.AddressXMLAdapter;
import net.sf.borg.model.db.BeanDB;
import net.sf.borg.model.db.jdbc.AddrJdbcDB;

public class AddressModel extends Model {

    private BeanDB<Address> db_; // the database

    public BeanDB<Address> getDB() {
        return (db_);
    }

    private HashMap<Integer, LinkedList<Address>> bdmap_ = new HashMap<Integer, LinkedList<Address>>();

    static private AddressModel self_ = null;

    private void load_map() {

        // clear map
        bdmap_.clear();

        try {

            // iterate through tasks using taskmodel
            Collection<Address> addrs = getAddresses();
            for (Address addr : addrs) {

                if (addr.getDeleted())
                    continue;

                // use birthday to build a day key
                Date bd = addr.getBirthday();
                if (bd == null)
                    continue;

                GregorianCalendar g = new GregorianCalendar();
                g.setTime(bd);

                int key = AppointmentModel.dkey(g);
                int bdkey = AppointmentModel.birthdayKey(key);

                // add the task string to the btmap_
                // add the task to the mrs_ Vector. This is used by the todo gui
                LinkedList<Address> o = bdmap_.get(new Integer(bdkey));
                if (o == null) {
                    o = new LinkedList<Address>();
                    bdmap_.put(new Integer(bdkey), o);
                }

                o.add(addr);
            }

        } catch (Exception e) {

            Errmsg.errmsg(e);
            return;
        }

    }

    public static AddressModel getReference() {
        return (self_);
    }

    public static AddressModel create() {
        self_ = new AddressModel();
        return (self_);
    }

    public void remove() {
        removeListeners();
        try {
            if (db_ != null)
                db_.close();
        } catch (Exception e) {
            Errmsg.errmsg(e);
            return;
        }
        db_ = null;
    }

    public Collection<Address> getAddresses() throws Exception {
        Collection<Address> addrs = db_.readAll();
        Iterator<Address> it = addrs.iterator();
        while (it.hasNext()) {
            Address addr = it.next();
            if (addr.getDeleted())
                it.remove();
        }
        return addrs;
    }

    public Collection<Address> getDeletedAddresses() throws Exception {
        Collection<Address> addrs = db_.readAll();
        Iterator<Address> it = addrs.iterator();
        while (it.hasNext()) {
            Address addr = it.next();
            if (!addr.getDeleted())
                it.remove();
        }
        return addrs;
    }

    public Collection<Address> getAddresses(int daykey) {
        // don't consider year for birthdays
        int bdkey = AppointmentModel.birthdayKey(daykey);
        // System.out.println("bdkey is " + bdkey);
        return (bdmap_.get(new Integer(bdkey)));
    }

    // open the SMDB database
    @SuppressWarnings("unchecked")
    public void open_db(String url) throws Exception {
        db_ = new AddrJdbcDB(url);
        load_map();
    }

    public void delete(Address addr, boolean refresh) throws Exception {

        try {
            LinkModel.getReference().deleteLinks(addr);
            String sync = Prefs.getPref(PrefName.PALM_SYNC);
            if (sync.equals("true")) {
                addr.setDeleted(true);
                db_.updateObj(addr);
            } else {
                db_.delete(addr.getKey());
            }
        } catch (Exception e) {
            Errmsg.errmsg(e);
        }

        if (refresh)
            refresh();
    }

    public void saveAddress(Address addr) throws Exception {
        saveAddress(addr, false);
    }

    public void saveAddress(Address addr, boolean sync) throws Exception {

        int num = addr.getKey();

        if (num == -1) {
            int newkey = db_.nextkey();
            addr.setKey(newkey);
            if (!sync) {
                addr.setNew(true);
            }
            db_.addObj(addr);

        } else {

            if (!sync) {
                addr.setModified(true);
            }
            db_.updateObj(addr);

        }

        // inform views of data change
        refresh();
    }

    // allocate a new Row from the DB
    public Address newAddress() {
        return (db_.newObj());
    }

    public Address getAddress(int num) throws Exception {
        return (db_.readObj(num));
    }

    public void export(Writer fw) throws Exception {

        // FileWriter fw = new FileWriter(fname);
        fw.write("<ADDRESSES>\n");
        AddressXMLAdapter ta = new AddressXMLAdapter();


        // export addresses

        Collection<Address> addrs = getAddresses();
        for( Address addr : addrs ) {

            XTree xt = ta.toXml(addr);
            fw.write(xt.toString());
        }

        fw.write("</ADDRESSES>");

    }

    public void importXml(XTree xt) throws Exception {

        AddressXMLAdapter aa = new AddressXMLAdapter();

        for (int i = 1;; i++) {
            XTree ch = xt.child(i);
            if (ch == null)
                break;

            if (!ch.name().equals("Address"))
                continue;
            Address addr = aa.fromXml(ch);
            addr.setKey(-1);
            saveAddress(addr);
        }

        refresh();
    }

    public void refresh() {
        load_map();
        refreshListeners();
    }

    public void sync() {
        db_.sync();
        refresh();
    }
}
