/*
 * Project Name: Scriptor
 *
 * Licensed under the PolyForm Shield License 1.0.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * https://polyformproject.org/wp-content/uploads/2020/06/PolyForm-Shield-1.0.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nightfall.scriptor.exceptions;

/**
 * Represents a marker interface for all command-related exception types.
 *
 * This interface serves as a common type for various exception types that are
 * associated with command execution and parsing. It is intended to be implemented
 * by specific exception types that define error conditions related to commands
 * in a command processing framework.
 *
 * <p>Implementing classes should provide details about the specific type of exception
 * (such as invalid argument type, missing parameters, unknown commands, etc.) to
 * help the framework handle and report errors in a consistent manner.
 *
 * <p>Example of implementing classes include:
 * <ul>
 *     <li>{@link com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType} - for errors related to dynamic commands.</li>
 *     <li>{@link SimpleCommandExceptionType} - for simpler, more general command errors.</li>
 * </ul>
 *
 * @see com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType
 * @see SimpleCommandExceptionType
 */
public interface CommandExceptionType {
}

