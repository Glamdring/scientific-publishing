/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.util;

import java.util.Map;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class HibernateExtendedJpaVendorAdapter extends HibernateJpaVendorAdapter {
    private Map<String, Object> vendorProperties;

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> properties = super.getJpaPropertyMap();
        properties.putAll(vendorProperties);
        return properties;
    }

    public Map<String, Object> getVendorProperties() {
        return vendorProperties;
    }

    public void setVendorProperties(Map<String, Object> vendorProperties) {
        this.vendorProperties = vendorProperties;
    }
}